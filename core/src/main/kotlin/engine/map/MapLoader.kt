package engine.map

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.Rectangle
import org.cluelesshog.game.asset.TextureUtils

object MapLoader {
    fun load(path: String): GameMap {
        val map = TmxMapLoader().load(path)
        val gameMap = GameMap(map)

//        gameMap.apply {
//            // создаем новый слой
//            val borderLayer = TiledMapTileLayer(mapWidth, mapHeight, tileWidth, tileHeight)
//            borderLayer.name = "collision"
//
//            // можно задать “пустой” тайл (без текстуры)
//            val invisibleTile = StaticTiledMapTile(TextureUtils.loadInvisibleTexture().region) // без текстуры
//
//            // сверху и снизу
//            for (x in 0 until mapWidth) {
//                borderLayer.setCell(x, 0, TiledMapTileLayer.Cell().setTile(invisibleTile))           // нижняя граница
//                borderLayer.setCell(x, mapHeight - 1, TiledMapTileLayer.Cell().setTile(invisibleTile)) // верхняя граница
//            }
//
//            // слева и справа
//            for (y in 0 until mapHeight) {
//                borderLayer.setCell(0, y, TiledMapTileLayer.Cell().setTile(invisibleTile))           // левая
//                borderLayer.setCell(mapWidth - 1, y, TiledMapTileLayer.Cell().setTile(invisibleTile)) // правая
//            }
//
//            // добавляем слой в карту
//            map.layers.add(borderLayer)
//        }


        map.layers.get("collision")?.objects?.forEach {
            when (it) {
                is RectangleMapObject -> {
                    gameMap.collisionRects.add(it.rectangle)
                }
                is PolygonMapObject -> {
                    gameMap.collisionRects.add(it.polygon.boundingRectangle)
                }
            }
        }

        map.layers.get("portals")?.objects?.forEach {
            if (it is RectangleMapObject) {
                val props = it.properties
                gameMap.portals.add(
                    Portal(
                        rect = it.rectangle,
                        targetMap = props["targetMap", String::class.java],
                        targetX = props["targetX", Float::class.java],
                        targetY = props["targetY", Float::class.java],
                    )
                )
            }
        }

        // интерактивные объекты
        map.layers.get("objects")?.objects?.forEach {
            val type = it.properties["type", String::class.java] ?: return@forEach
            val rect = (it as? RectangleMapObject)?.rectangle ?: return@forEach

            gameMap.objects.add(InteractiveObject(type, rect))
        }

        return gameMap
    }
}
