package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.scenes.scene2d.ui.Image
import org.cluelesshog.game.asset.TextureUtils.getHighlightTexture
import org.cluelesshog.game.asset.TextureUtils.loadTextureForJewelType
import org.cluelesshog.game.logic.Jewel
import org.cluelesshog.game.logic.JewelPos

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

    fun highlight() {
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
        setPosition(jewel.pos.column * (width + 5), jewel.pos.row * (height + 5))
    }

    fun getCoordinates(): JewelPos {
        return jewel.pos
    }
}
