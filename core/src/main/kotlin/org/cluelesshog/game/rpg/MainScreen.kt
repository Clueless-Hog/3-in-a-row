package org.cluelesshog.game.rpg

import engine.Scene
import engine.map.GameMap
import engine.map.MapLoader
import engine.map.MapStage

class MainScreen(private val map: GameMap = MapLoader.load("maps/test_map.tmx")) : Scene(wrapper = MapStage(map)) {
    override fun load(): Boolean {
        val player = PlayerActor(map)
        wrapper.addListener(MoveInputProcessor(player))

        player.setSize(100f, 100f)
        player.setPosition(200f, 200f)

        wrapper.addActor(player)

        return true
    }
}
