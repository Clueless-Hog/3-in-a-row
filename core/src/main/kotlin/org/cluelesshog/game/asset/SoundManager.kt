package org.cluelesshog.game.asset

import engine.asset.AssetLoader
import com.badlogic.gdx.audio.Sound
import org.cluelesshog.game.settings.Settings

object SoundManager {
    private val cache = mutableMapOf<SoundType, Sound>()
    var volume = Settings.volume

    init {
        Settings.addObserver {
            volume = it.volume
        }
    }

    fun getSound(type: SoundType) =
        cache.getOrPut(type) {
            when (type) {
                SoundType.SWAP -> AssetLoader.getSound("zvuk3")
                SoundType.MATCH -> AssetLoader.getSound("swap1")
                SoundType.CLICK -> AssetLoader.getSound("click")
            }
        }

    fun playSound(type: SoundType) {
        getSound(type).play(volume)
    }

    fun playSound(type: SoundType, pitch: Float) {
        getSound(type).play(volume, pitch, 0f)
    }
}
