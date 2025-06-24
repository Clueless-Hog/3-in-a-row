package asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture

object AssetLoader {
    private val manager = AssetManager()

    private val tileFiles = Gdx.files.internal("tiles").list()
    private val soundFiles = Gdx.files.internal("sounds").list()

    init {
        for (file in tileFiles) {
            if (!file.name().endsWith(".png")) continue
            manager.load(file.path(), Texture::class.java)
        }
        for (file in soundFiles) {
            if (!file.name().endsWith(".wav")) continue
            manager.load(file.path(), Sound::class.java)
        }

        manager.finishLoading()
    }

    fun getTile(name: String): Texture {
        return manager.get("tiles/$name.png", Texture::class.java)
    }

    fun getSound(name: String): Sound {
        return manager.get("sounds/$name.wav", Sound::class.java)
    }
}
