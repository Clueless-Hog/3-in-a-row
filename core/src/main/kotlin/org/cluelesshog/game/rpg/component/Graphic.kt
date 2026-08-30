package org.cluelesshog.game.rpg.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion

class Graphic(
    var region: TextureRegion?,
    val color: Color
) : Component {
    companion object {
        val MAPPER = ComponentMapper.getFor(Graphic::class.java)!!
    }
}
