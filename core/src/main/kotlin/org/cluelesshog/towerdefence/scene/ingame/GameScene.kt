package org.cluelesshog.towerdefence.scene.ingame

import org.cluelesshog.game.Scene
import org.cluelesshog.engine.geometry.Position
import org.cluelesshog.towerdefence.Assets
import org.cluelesshog.towerdefence.tower.SmartAim

class GameScene: Scene() {
    private val spawnEnemyEveryXSeconds = 3
    private var timePassedSinceLastSpawn = 0f
    private val enemySpawnPoint = Position(1000, 600)
    private val pathToObjective = listOf(
        enemySpawnPoint.down(300),
        enemySpawnPoint.down(800).left(400),
        enemySpawnPoint.down(300).left(400).up(100).left(200),
    )

    override fun load(): Boolean {
        listOf(
            TowerSpot(Position(3, 3)),
            TowerSpot(Position(300, 3)),
            TowerSpot(Position(3, 600)),
        ).forEach { wrapper.addActor(it) }

        wrapper.addListener {
            if (it is TowerSpotClicked) {
                handleTowerPlacement(it.towerSpot)

                true
            }

            false
        }

        return true
    }

    override fun act(delta: Float) {
        timePassedSinceLastSpawn += delta
        // On lag might spawn fewer enemies than expected (like, 7 seconds passed but only 1 enemy spawned)
        if (timePassedSinceLastSpawn > spawnEnemyEveryXSeconds) {
            timePassedSinceLastSpawn = 0f
            spawnEnemy()
        }

        World.enemies.removeIf { !it.isAlive() }

        super.act(delta)
    }

    private fun handleTowerPlacement(towerSpot: TowerSpot) {
        towerSpot.isVisible = false

        wrapper.addActor(Tower(towerSpot.getPosition(), SmartAim()))
    }

    private fun spawnEnemy() {
        val enemy = Enemy(enemySpawnPoint, Assets.enemy, pathToObjective)
        wrapper.addActor(enemy)
        World.enemies.add(enemy)
    }
}
