package org.cluelesshog.engine.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture

object AssetLoader {
    private val manager = AssetManager()

    private val tileFiles = Gdx.files.internal("tiles").list().filter { it.name().endsWith(".png") }
    private val soundFiles = Gdx.files.internal("sounds").list().filter { it.name().endsWith(".wav") }

    init {
        tileFiles.forEach { manager.load(it.path(), Texture::class.java) }
        soundFiles.forEach { manager.load(it.path(), Sound::class.java) }

        manager.finishLoading()
    }

    fun getTile(name: String): Texture {
        return manager.get("tiles/$name.png", Texture::class.java)
    }

    fun getSound(name: String): Sound {
        return manager.get("sounds/$name.wav", Sound::class.java)
    }
}
