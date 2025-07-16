package org.cluelesshog.towerdefence.scene.ingame

import org.cluelesshog.engine.actor.Player

object World {
    val enemies = mutableListOf<Enemy>()
    lateinit var player: Player
}
