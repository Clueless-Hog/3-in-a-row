package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo
import com.badlogic.gdx.scenes.scene2d.ui.Image
import org.cluelesshog.game.asset.TextureUtils.loadTextureForJewelType
import org.cluelesshog.game.logic.Jewel

data class JewelActor(
    private var jewel: Jewel,
    val size: Float
) : Image(loadTextureForJewelType(jewel.type)) {
    init {
        setSize(size, size)
        updatePosition()
        setOrigin(width / 2, height / 2)
        setZIndex(1)
    }

    fun highlight() {
        addAction(Actions.scaleTo(1.2f, 1.2f, .05f, Interpolation.ExpOut(2f, 3f)))
        setZIndex(100)
    }

    fun unhighlight() {
        addAction(Actions.scaleTo(1f, 1f, .05f, Interpolation.ExpOut(2f, 3f)))
        setZIndex(1)
    }

    fun update() {
        updatePosition()
    }

    fun getPos() = jewel.pos

    private fun updatePosition() {
        setPosition(jewel.pos.column * (width + 5), jewel.pos.row * (height + 5))
    }
}
