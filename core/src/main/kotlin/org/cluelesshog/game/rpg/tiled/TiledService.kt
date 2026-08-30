package org.cluelesshog.game.rpg.tiled

import com.badlogic.gdx.maps.tiled.TiledMap
import java.util.function.Consumer

class TiledService {
    private lateinit var currentMap: TiledMap

    private lateinit var mapChangeConsumer: Consumer<TiledMap>
}
