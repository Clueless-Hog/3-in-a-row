package org.cluelesshog.towerdefence.scene.ingame

import com.badlogic.gdx.graphics.g2d.Sprite
import org.cluelesshog.engine.actor.SpritedActor
import org.cluelesshog.engine.geometry.Position
import org.cluelesshog.towerdefence.tower.Target
import kotlin.math.max
import kotlin.math.min

class Enemy(position: Position, texture: Sprite, val path: List<Position>) : Target, SpritedActor(position, texture) {
    val speed = 200f

    var health = 15
        set(value) {
            // don't let health drop lower than 0
            field = max(value, 0)
            if (field == 0) {
                remove()
            }
        }

    var currentlyMovingTowardsRef = 0
    var currentlyMovingTowards: Position? = path[currentlyMovingTowardsRef]

    override fun act(delta: Float) {
        super.act(delta)

        if (!isAlive()) {
            return
        }

        if (currentlyMovingTowards == null) {
            return
        }

        val destination = currentlyMovingTowards!!
        var dx = 0f
        var dy = 0f
        if (destination.x > x) {
            dx += min(speed * delta, destination.x - x)
        }

        if (destination.x < x) {
            dx -= min(speed * delta, x - destination.x)
        }

        if (destination.y > y) {
            dy += min(speed * delta, destination.y - y)
        }

        if (destination.y < y) {
            dy -= min(speed * delta, y - destination.y)
        }

        when {
            dx != 0f -> moveBy(dx, 0f)
            dy != 0f -> moveBy(0f, dy)
            currentlyMovingTowards != null -> {
                currentlyMovingTowards = path.getOrNull(++currentlyMovingTowardsRef)
            }
        }
    }

    fun isAlive() = health > 0

    override fun movingTowards(): Position {
        return currentlyMovingTowards ?: path.first()
    }

    override fun speed(): Float {
        if (currentlyMovingTowards == null) {
            return 0f
        }

        return speed
    }
}
