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
    val range = 1400

    val projectileSpeed = 1000f

    var attackSpeedModifier = 1f

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

        attackSpeedModifier = if (collides(World.player)) 8f else 1f
    }

    private fun isInReach(enemy: Enemy): Boolean {
        val midX = enemy.x - x
        val midY = enemy.y - y

        return sqrt((midX * midX) + (midY * midY)) <= range
    }

    private fun attack(enemy: Enemy) {
        untilReloadFinished = 1f / (attacksPerSecond * attackSpeedModifier)
        val spawnAt = getPosition()
        val aimAt = aim.predict(spawnAt, enemy, projectileSpeed)
        val projectile = Projectile(damage, projectileSpeed, spawnAt, aimAt)

        parent.addActor(projectile)
    }
}
