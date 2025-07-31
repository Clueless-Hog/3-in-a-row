package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.scenes.scene2d.ui.Image
import org.cluelesshog.game.asset.TextureUtils.getHighlightTexture
import org.cluelesshog.game.asset.TextureUtils.loadTextureForJewelType
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.logic.Jewel
import org.cluelesshog.game.logic.JewelPos

class JewelActor(
    private var jewel: Jewel,
    size: Float,
    private val x: Int = jewel.pos.column,
    private val y: Int = jewel.pos.row
) : Image(loadTextureForJewelType(jewel.type)) {
    private var originalTexture = loadTextureForJewelType(jewel.type)
    private var highlightTexture = getHighlightTexture(drawable)

    var isClicked = false

    init {
        setSize(size, size)
        setOrigin(width / 2, height / 2)
        setPosition(jewel.pos.column * (width + 5), jewel.pos.row * (height + 5))
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

    fun update(board: Board) {
        jewel = board.getJewel(x, y)

        drawable = loadTextureForJewelType(jewel.type)
        originalTexture = loadTextureForJewelType(jewel.type)
        highlightTexture = getHighlightTexture(drawable)
    }

    fun getPos(): JewelPos {
        return jewel.pos
    }
}
