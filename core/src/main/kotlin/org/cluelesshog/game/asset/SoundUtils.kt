package org.cluelesshog.game.asset

import asset.AssetLoader
import com.badlogic.gdx.audio.Sound
import org.cluelesshog.game.scene.gamescreen.SoundType

object SoundUtils {
    private val cache = mutableMapOf<SoundType, Sound>()

    fun getSound(type: SoundType) =
        cache.getOrPut(type) {
            when (type) {
                SoundType.SWAP -> AssetLoader.getSound("zvuk3")
                SoundType.MATCH -> AssetLoader.getSound("swap1")
            }
        }

    fun playSound(type: SoundType, volume: Float) {
        getSound(type).play(volume)
    }

    fun playSound(type: SoundType, volume: Float = 1f, pitch: Float) {
        getSound(type).play(volume, pitch, 0f)
    }

    fun playSound(type: SoundType) {
        playSound(type, 1f)
    }
}
