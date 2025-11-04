package org.cluelesshog.game.match3.view

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import org.cluelesshog.game.asset.TextureUtils.loadTexture
import org.cluelesshog.game.match3.logic.Jewel
import org.cluelesshog.game.match3.logic.JewelPos

data class JewelActor(
    val jewel: Jewel,
    val size: Float,
    var pos: JewelPos = jewel.pos,
) : Image(loadTexture(jewel.type.name.lowercase())) {
    init {
        setSize(size, size)
        setPosition(pos.column * width, pos.row * height)
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
