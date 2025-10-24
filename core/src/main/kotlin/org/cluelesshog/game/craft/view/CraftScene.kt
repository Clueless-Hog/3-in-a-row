package org.cluelesshog.game.craft.view

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import engine.SceneController
import ktx.actors.onClick
import org.cluelesshog.game.inventory.view.InventoryView
import engine.Scene
import org.cluelesshog.game.inventory.Inventory
import org.cluelesshog.game.inventory.ItemRef
import org.cluelesshog.game.match3.view.GameScreen

class CraftScene: Scene() {
    private val boardSize: Float
        get() = minOf(getScreenWidth() * 0.8f, getScreenHeight() * 0.8f)

    override fun load(): Boolean {
        val dimension = 5
        val slotSize = boardSize/dimension
        val inventoryView = InventoryView(dimension, slotSize, Inventory)

        fun TextButton.revert() {
            this.touchable = Touchable.disabled
            this.setText("Sell")
        }

        val sellItemButton = TextButton("", theme).also { it.revert() }

        inventoryView.onSlotSelected { selectedSlot ->
            val slotInfo = selectedSlot.slot
            if (slotInfo.item == null || slotInfo.item == ItemRef.GOLD) {
                return@onSlotSelected
            }

            if (selectedSlot.isHighlighted) {
                sellItemButton.touchable = Touchable.enabled
                val price = 100 * slotInfo.quantity // hardcoded
                sellItemButton.setText("Sell for ${price}G)")

                sellItemButton.clearListeners()
                sellItemButton.onClick {
                    sellItemButton.revert()
                    Inventory.remove(slotInfo.item!!, slotInfo.quantity)
                    Inventory.store(ItemRef.GOLD, price)
                }
            } else {
                sellItemButton.revert()
            }
        }

        createHud(sellItemButton)

        wrapper.addActor(inventoryView)

        return true
    }

    override fun dispose() {
        Inventory.save()

        super.dispose()
    }

    private fun createHud(sellItemButton: TextButton) {
        // TODO зачем нужен отдельный враппер на hud? Он же ничего не делает, кажется
        val boardSceneSwitcher = TextButton("<< Board", theme)
        boardSceneSwitcher.onClick {
            SceneController.set<GameScreen>()
        }

        val root = Table().apply {
            setFillParent(true)
            right()

            columnDefaults(0).width(getScreenWidth() * 0.1f)
            columnDefaults(1).width(getScreenWidth() * 0.15f)

            add(boardSceneSwitcher).height(40f).row()

            add(sellItemButton).height(40f).row()
        }

        hud.addActor(root)
    }
}
