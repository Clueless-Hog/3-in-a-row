package engine.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.audio.Sound

enum class SoundAsset(musicFile: String) : Asset<Sound> {
    SWORD_HIT("sword_hit.wav"),
    LIFE_REG("life_reg.wav"),
    TRAP("trap.wav"),
    SWING("swing.wav"),
    ;

    private val descriptor = AssetDescriptor("audio/$musicFile", Sound::class.java)

    override fun getDescriptor() = descriptor
}
