package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.scenes.scene2d.ui.Image
import org.cluelesshog.game.asset.TextureUtils.getHighlightTexture
import org.cluelesshog.game.asset.TextureUtils.loadTextureForJewelType
import org.cluelesshog.game.logic.Jewel

class JewelActor(
    private val jewel: Jewel,
    size: Float
) : Image(loadTextureForJewelType(jewel.type)) {
    private var originalTexture = loadTextureForJewelType(jewel.type)
    private var highlightTexture = getHighlightTexture(drawable)

    var isClicked = false

    init {
        setSize(size, size)
        setOrigin(width / 2, height / 2)
        updatePosition()
        zIndex = 0
    }

    fun toggleSelection() {
        if (!isClicked) highlight() else unhighlight()
    }

    fun highlight() {
        println("Pos: ${getCoordinates()}, Status: $isClicked")
        isClicked = true
        setDrawable(highlightTexture)
        setScale(1.2f)
        setZIndex(100)
    }

    fun unhighlight() {
        isClicked = false
        setDrawable(originalTexture)
        setScale(1f)
        setZIndex(1)
    }

    fun updatePosition() {
        setPosition(jewel.column * (width + 5), jewel.row * (height + 5))
    }

    fun getCoordinates(): Pair<Int, Int> {
        return jewel.column to jewel.row
    }
}
