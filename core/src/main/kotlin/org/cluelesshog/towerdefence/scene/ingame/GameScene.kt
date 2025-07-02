package org.cluelesshog.towerdefence.scene.ingame

import org.cluelesshog.game.Scene
import org.cluelesshog.engine.geometry.Position
import org.cluelesshog.towerdefence.Assets

class GameScene: Scene() {
    private val spawnEnemyEveryXSeconds = 3
    private var timePassedSinceLastSpawn = 0f
    private val enemySpawnPoint = Position(1000, 600)
    private val pathToObjective = listOf(
        enemySpawnPoint.down(300),
        enemySpawnPoint.down(300).left(400),
        enemySpawnPoint.down(300).left(400).down(100),
        enemySpawnPoint.down(300).left(400).down(100).left(200),
    )

    override fun load(): Boolean {
        val spot = TowerSpot(Position(3, 3))

        wrapper.addActor(spot)

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
        towerSpot.remove()

        wrapper.addActor(Tower(towerSpot.getPosition()))
    }

    private fun spawnEnemy() {
        val enemy = Enemy(enemySpawnPoint, Assets.enemy, pathToObjective)
        wrapper.addActor(enemy)
        World.enemies.add(enemy)
    }
}
