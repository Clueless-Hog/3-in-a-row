package org.cluelesshog.game.rpg.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.math.Vector2

class Transform(
    val position: Vector2,
    val z: Int,
    val size: Vector2 = Vector2(1f, 1f),
    val scaling: Vector2 = Vector2(1f, 1f),
    var rotationDeg: Float = 0f,
    var sortOffsetY: Float
) : Component, Comparable<Transform> {
    override fun compareTo(other: Transform): Int {
        if (z != other.z) return z.compareTo(other.z)

        if (position.y + sortOffsetY != other.position.y + other.sortOffsetY) {
            return (other.position.y + other.sortOffsetY).compareTo(
                position.y + sortOffsetY
            )
        }

        return position.x.compareTo(other.position.x)
    }

    companion object {
        val MAPPER = ComponentMapper.getFor(Transform::class.java)!!
    }
}
