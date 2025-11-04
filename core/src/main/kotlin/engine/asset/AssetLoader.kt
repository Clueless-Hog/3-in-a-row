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
            if (file.name().endsWith(".png")) {
                manager.load(file.path(), Texture::class.java)
            } else if (file.name().endsWith(".wav")) {
                manager.load(file.path(), Sound::class.java)
            }
        }

        manager.finishLoading()
    }

    fun getTile(name: String): Texture {
        return manager.get("tiles/$name.png", Texture::class.java)
    }

    fun getSound(name: String): Sound {
        return manager.get("sounds/$name.wav", Sound::class.java)
    }

    // @TODO сделать нормальный загрузчик
    fun getAtlas(name: String): TextureAtlas {
        return TextureAtlas(Gdx.files.internal("sprites/$name.atlas"))
    }
}
