package engine.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader

enum class MapAsset(mapName: String) : Asset<TiledMap>{
    MAIN("test_map.tmx");

    private val descriptor: AssetDescriptor<TiledMap>

    init {
        val parameters = TmxMapLoader.Parameters()
        parameters.projectFilePath = "maps16x16/maps16x16.tiled-project"
        descriptor = AssetDescriptor<TiledMap>("maps16x16/$mapName", TiledMap::class.java, parameters)
    }

    override fun getDescriptor() = descriptor
}
