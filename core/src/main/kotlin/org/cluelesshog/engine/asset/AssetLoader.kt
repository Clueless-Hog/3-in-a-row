package org.cluelesshog.engine.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool

object AssetLoader {
    private val manager = AssetManager()

    private val tileFiles = Gdx.files.internal("tiles").list().filter { it.name().endsWith(".png") }
    private val soundFiles = Gdx.files.internal("audio").list().filter { it.name().endsWith(".wav") }
    private val musicFiles = Gdx.files.internal("audio").list().filter { it.name().endsWith(".mp3") }

    private val explosionEffectPool: ParticleEffectPool

    init {
        tileFiles.forEach { manager.load(it.path(), Texture::class.java) }
        soundFiles.forEach { manager.load(it.path(), Sound::class.java) }
        musicFiles.forEach { manager.load(it.path(), Music::class.java) }

        val explosionEffect = ParticleEffect()
        explosionEffect.load(
            Gdx.files.internal("effects/explosion.p"),
            Gdx.files.internal("effects")
        )
        explosionEffectPool = ParticleEffectPool(explosionEffect, 5, 20)

        manager.finishLoading()
    }

    fun getTile(name: String): Texture {
        return manager.get("tiles/$name.png", Texture::class.java)
    }

    fun getSound(name: String): Sound {
        return manager.get("audio/$name.wav", Sound::class.java)
    }

    fun getMusic(name: String): Music {
        return manager.get("audio/$name.mp3", Music::class.java)
    }

    fun getExplosionEffect(): ParticleEffectPool.PooledEffect {
        return explosionEffectPool.obtain()
    }
}
