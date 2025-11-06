package engine.map

import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TmxMapLoader

object MapLoader {
    fun load(path: String): GameMap {
        val map = TmxMapLoader().load(path)
        val gameMap = GameMap(map)

        map.layers.get("collision")?.objects?.forEach {
            if (it is RectangleMapObject) {
                gameMap.collisionRects.add(it.rectangle)
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
