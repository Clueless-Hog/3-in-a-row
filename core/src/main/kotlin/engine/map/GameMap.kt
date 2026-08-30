package engine.map

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Rectangle

class GameMap(val map: TiledMap) {
    val mapWidth = map.properties.get("width", Int::class.java)!!
    val mapHeight = map.properties.get("height", Int::class.java)!!
    val tileWidth = map.properties.get("tilewidth", Int::class.java)!!
    val tileHeight = map.properties.get("tileheight", Int::class.java)!!

    val widthPx = mapWidth * tileWidth
    val heightPx = mapHeight * tileHeight

    val collisionRects = mutableListOf<Rectangle>()
    val portals = mutableListOf<Portal>()
    val objects = mutableListOf<InteractiveObject>()
}
