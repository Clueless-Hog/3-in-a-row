package engine.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas

object AssetLoader {
    private val manager = AssetManager()

    private val assetList = Gdx.files.internal("assets.txt")
        .readString()
        .lines()
        .filter { it.isNotBlank() }

    init {
        assetList.forEach {
            val file = Gdx.files.internal(it)
            when (file.extension()) {
                "png" -> manager.load(file.path(), Texture::class.java)
                "atlas" -> manager.load(file.path(), TextureAtlas::class.java)
                "wav" -> manager.load(file.path(), Sound::class.java)
            }
        }

        manager.finishLoading()
    }

    fun getTile(name: String): Texture = manager.get("tiles/$name.png")

    fun getSound(name: String): Sound = manager.get("sounds/$name.wav")

    fun getAtlas(name: String): TextureAtlas = manager.get("sprites/$name.atlas")
}
