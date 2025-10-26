package org.cluelesshog.game.craft.view

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import engine.SceneController
import ktx.actors.onClick
import org.cluelesshog.game.inventory.view.InventoryView
import engine.Scene
import engine.event.listen
import ktx.actors.onChange
import org.cluelesshog.game.craft.RecipeRef
import org.cluelesshog.game.inventory.Inventory
import org.cluelesshog.game.inventory.ItemRef
import org.cluelesshog.game.inventory.event.ItemAdded
import org.cluelesshog.game.inventory.event.ItemRemoved
import org.cluelesshog.game.inventory.view.SlotView
import org.cluelesshog.game.match3.view.GameScreen

fun TextButton.hide() {
    this.touchable = Touchable.disabled
    this.isVisible = false
}

fun TextButton.show() {
    this.touchable = Touchable.enabled
    this.isVisible = true
}

class CraftScene: Scene() {
    private val boardSize: Float
        get() = minOf(getScreenWidth() * 0.8f, getScreenHeight() * 0.8f)

    private val recipeList = SelectBox<String>(theme)
    private val sellItemButton = TextButton("", theme).also { it.hide() }
    private val craftItemButton = TextButton("Craft", theme).also {
        it.hide()
        it.onClick { performCraft() }
    }

    val nothing = "select item to craft"

    private val inventory = Inventory

    override fun load(): Boolean {
        val dimension = 5
        val slotSize = boardSize/dimension
        val inventoryView = InventoryView(dimension, slotSize, inventory)

        inventoryView.onSlotClicked { onInventorySlotClicked(it) }

        createHud()

        wrapper.addActor(inventoryView)

        return true
    }

    override fun dispose() {
        inventory.save()

        super.dispose()
    }

    private fun createHud() {
        // TODO зачем нужен отдельный враппер на hud? Он же ничего не делает, кажется
        val boardSceneSwitcher = TextButton("<< Board", theme)
        boardSceneSwitcher.onClick {
            SceneController.set<GameScreen>()
        }

        recipeList.onChange {
            if (recipeList.selected != null && recipeList.selected != nothing) {
                craftItemButton.show()
            }
        }
        refreshCraftOptions()

        listen<ItemAdded> { refreshCraftOptions() }
        listen<ItemRemoved> { refreshCraftOptions() }

        val root = Table().apply {
            setFillParent(true)
            right()

            columnDefaults(0).width(getScreenWidth() * 0.1f)
            columnDefaults(1).width(getScreenWidth() * 0.15f)

            add(boardSceneSwitcher).height(40f).row()

            add(sellItemButton).height(40f).width(200f).row()
            add(recipeList).height(40f).width(200f).row()
            add(craftItemButton).height(40f).width(200f).row()
        }

        hud.addActor(root)
    }

    private fun onInventorySlotClicked(selectedSlot: SlotView) {
        val slotInfo = selectedSlot.slot
        if (slotInfo.item == null) {
            return
        }

        if (slotInfo.item == ItemRef.GOLD) {
            sellItemButton.hide()

            return
        }

        if (selectedSlot.isHighlighted) {
            sellItemButton.show()
            val price = 100 * slotInfo.quantity // hardcoded
            sellItemButton.setText("Sell for ${price}G)")

            sellItemButton.clearListeners()
            sellItemButton.onClick {
                sellItemButton.hide()
                inventory.remove(slotInfo.item!!, slotInfo.quantity)
                inventory.store(ItemRef.GOLD, price)
            }
        } else {
            sellItemButton.hide()
        }
    }

    private fun refreshCraftOptions() {
        val craftableRecipes = RecipeRef.all.filter { recipe ->
            recipe.requirements.all { req ->
                inventory.findSlot(req.item)?.quantity?.let { it >= req.quantity } ?: false
            }
        }

        if (craftableRecipes.isNotEmpty()) {
            if (recipeList.selected != null && recipeList.selected != nothing) {
                craftItemButton.show()
            }
        } else {
            craftItemButton.hide()
        }

        val options = listOf(nothing) + craftableRecipes.map { it.name() }
        recipeList.setItems(*options.toTypedArray())
    }

    private fun performCraft() {
        RecipeRef.all
            .firstOrNull { recipe -> recipe.name() == recipeList.selected }
            ?.apply {
                requirements.forEach { required -> inventory.remove(required.item, required.quantity) }
                inventory.store(result.item, result.quantity)
            }
    }
}
