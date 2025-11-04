package org.cluelesshog.game.inventory.view

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import ktx.scene2d.textTooltip
import org.cluelesshog.game.asset.TextureUtils
import org.cluelesshog.game.inventory.InventorySlot
import org.cluelesshog.game.inventory.Item

class SlotView(val slot: InventorySlot): Group() {
    private val tooltip: TextTooltip = textTooltip("")
    private val backgroundImage = Image(TextureUtils.loadTexture("empty-slot")).also {
        it.setFillParent(true)
    }

    private val itemImage = Image().also {
        it.zIndex = 1
        it.setFillParent(true)
    }
    var isHighlighted = false
        private set

    init {
        addActor(backgroundImage)
        addActor(itemImage)
        update()
    }

    companion object {
        private fun loadItemTexture(item: Item?): TextureRegionDrawable? {
            val atlasName= "Shikashi's Fantasy Icons Pack v2/#2 - Transparent Icons & Drop Shadow"
            return when {
                item == null -> null
                else -> TextureUtils.loadTexture(item.name.lowercase(), atlasName)
            }
        }
    }

    fun update() {
        itemImage.drawable = loadItemTexture(slot.item)
        changeTooltip(slot.label())
    }

    fun highlight() {
        if (slot.isEmpty()) {
            return
        }
        isHighlighted = true

        itemImage.setZIndex(100)
        itemImage.addAction(
            Actions.sequence(
                Actions.scaleTo(1.2f, 1.2f, .05f, Interpolation.ExpOut(2f, 3f)),
                Actions.scaleTo(1.1f, 1.1f, .05f, Interpolation.ExpOut(2f, 3f)),
                Actions.scaleTo(1.15f, 1.15f, .05f, Interpolation.ExpOut(2f, 3f)),
            )
        )
    }

    fun unhighlight() {
        itemImage.setZIndex(1)
        itemImage.addAction(Actions.scaleTo(1f, 1f, .05f, Interpolation.ExpOut(2f, 3f)))

        isHighlighted = false
    }

    private fun changeTooltip(tip: String) {
        tooltip.actor.setText(tip)
    }
}
