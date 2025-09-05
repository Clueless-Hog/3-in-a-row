package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import org.cluelesshog.game.asset.TextureUtils.loadTextureForJewelType
import org.cluelesshog.game.logic.Jewel
import org.cluelesshog.game.logic.JewelPos

data class JewelActor(
    val jewel: Jewel,
    val size: Float,
    var pos: JewelPos = jewel.pos,
) : Image(loadTextureForJewelType(jewel.type)) {
    init {
        setSize(size, size)
        setPosition(pos.column * (this.width), pos.row * (this.height))
        setOrigin(width / 2, height / 2)
        setZIndex(1)
    }

    fun highlight() {
        addAction(
            Actions.sequence(
                Actions.scaleTo(1.2f, 1.2f, .05f, Interpolation.ExpOut(2f, 3f)),
                Actions.scaleTo(1.1f, 1.1f, .05f, Interpolation.ExpOut(2f, 3f)),
                Actions.scaleTo(1.15f, 1.15f, .05f, Interpolation.ExpOut(2f, 3f)),
            )
        )
        setZIndex(100)
    }

    fun unhighlight() {
        addAction(Actions.scaleTo(1f, 1f, .05f, Interpolation.ExpOut(2f, 3f)))
        setZIndex(1)
    }
}
