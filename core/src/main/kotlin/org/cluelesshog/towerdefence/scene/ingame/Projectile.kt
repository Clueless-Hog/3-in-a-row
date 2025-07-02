package org.cluelesshog.towerdefence.scene.ingame

import org.cluelesshog.engine.actor.SpritedActor
import org.cluelesshog.engine.geometry.Angle
import org.cluelesshog.engine.geometry.Position
import org.cluelesshog.towerdefence.Assets
import kotlin.math.cos
import kotlin.math.sin

class Projectile(private val damage: Int, position: Position, vector: Position): SpritedActor(position, Assets.bullet) {
    private val speed = 600f

    private val vx: Float
    private val vy: Float

    init {
        val angle = Angle.between(position, vector)

        rotation = angle.degrees + -90

        vx = cos(angle.radians) * speed
        vy = sin(angle.radians) * speed
    }

    override fun act(delta: Float) {
        super.act(delta)

        checkCollision()

        moveBy(delta * vx, delta * vy)
    }

    private fun checkCollision() {
        World.enemies.forEach {
            if (collides(it)) {
                remove()
                it.health -= damage
            }
        }
    }
}
