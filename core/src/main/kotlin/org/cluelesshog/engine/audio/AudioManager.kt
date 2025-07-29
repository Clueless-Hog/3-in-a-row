package org.cluelesshog.engine.audio

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound

object AudioManager {
    private var soundVolume = 1f

    fun playSound(sound: Sound) {
        sound.play(soundVolume)
    }

    fun playMusic(music: Music) {
        if (!music.isPlaying) {
            music.play()
        }
    }

    fun setSoundVolume(volume: Int) {
        require(volume <= 100 && volume >=0)

        soundVolume = (volume/100).toFloat()
        // TODO change volume of in-progress sounds/music
    }
}
