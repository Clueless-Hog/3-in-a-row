package org.cluelesshog.game.rpg.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import java.util.*

class Facing(var direction: FacingDirection) : Component {
    enum class FacingDirection {
        UP, DOWN, LEFT, RIGHT;

        val atlasKey = this.name.lowercase(Locale.getDefault())
    }

    companion object {
        val MAPPER = ComponentMapper.getFor<Facing>(Facing::class.java)
    }
}
