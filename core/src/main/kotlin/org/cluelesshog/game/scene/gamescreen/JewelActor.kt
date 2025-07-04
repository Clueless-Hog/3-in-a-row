package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.scenes.scene2d.ui.Image
import org.cluelesshog.game.asset.TextureUtils
import org.cluelesshog.game.asset.TextureUtils.getHighlightTexture
import org.cluelesshog.game.asset.TextureUtils.loadTextureForJewelType
import org.cluelesshog.game.logic.Jewel
import org.cluelesshog.game.logic.JewelType

class JewelActor(
    val jewel: Jewel,
    private val column: Int,
    private val row: Int,
    private val size: Float
) : Image(loadTextureForJewelType(jewel.type)) {
    private var originalTexture = drawable
    private var highlightTexture = getHighlightTexture(drawable)

    var isClicked = false

    init {
        setSize(size, size)
        setPosition(column * size, row * size)
    }

    fun toggleSelect() {
        isClicked = !isClicked
        if (isClicked) highlight() else unhighlight()
    }

    fun highlight() {
        drawable = highlightTexture

        val newJewelSize = size * 1.2f
        val cellPosX = column * size
        val cellPosY = row * size
        val posX = cellPosX - ((newJewelSize - size) / 2)
        val posY = cellPosY - ((newJewelSize - size) / 2)

        setSize(newJewelSize, newJewelSize)
        setPosition(posX, posY)
    }

    fun unhighlight() {
        isClicked = false
        drawable = originalTexture

        setSize(size, size)
        setPosition(column * size, row * size)
    }

    fun updateType(newType: JewelType) {
        if (jewel.type != newType) {
            jewel.type = newType
            val newDrawable = loadTextureForJewelType(newType)
            setDrawable(newDrawable)
            setSize(size, size)
            setPosition(column * size, row * size)
            originalTexture = drawable
            highlightTexture = getHighlightTexture(drawable)
            invalidate()
        }
    }
}
