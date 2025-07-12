package org.cluelesshog.towerdefence.scene.ingame

import org.cluelesshog.engine.actor.SpritedActor
import org.cluelesshog.engine.geometry.Angle
import org.cluelesshog.engine.geometry.Position
import org.cluelesshog.towerdefence.Assets
import org.cluelesshog.towerdefence.tower.Aim
import kotlin.math.max
import kotlin.math.sqrt

class Tower(position: Position, private val aim: Aim) : SpritedActor(position, Assets.tower) {
    val attacksPerSecond = 1.5f
    var untilReloadFinished = 0f
    var damage = 3
    var range = 1400

    fun isReloading() = untilReloadFinished != 0f

    override fun act(delta: Float) {
        super.act(delta)

        if (isReloading()) {
            untilReloadFinished = max(untilReloadFinished - delta, 0f)
        }

         World.enemies
            .find { it.isAlive() && isInReach(it) }
            ?.also {
                if (!isReloading()) {
                    attack(it)
                }
            }
            ?.also { rotation = Angle.between(it.getPosition(), getPosition()).degrees }
    }

    private fun isInReach(enemy: Enemy): Boolean {
        val midX = enemy.x - x
        val midY = enemy.y - y

        return sqrt((midX * midX) + (midY * midY)) <= range
    }

    private fun attack(enemy: Enemy) {
        untilReloadFinished = 1f / attacksPerSecond
        val spawnAt = getPosition()
        val projectile = Projectile(damage, spawnAt, aim.predict(spawnAt, enemy, 600f))

        parent.addActor(projectile)
    }
}
