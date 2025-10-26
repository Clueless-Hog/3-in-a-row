package engine

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion

class AnimationPool(private val atlas: TextureAtlas) {
    private val animations = HashMap<String, Animation<TextureRegion>>()

    fun createAnimation(
        name: String,
        baseName: String,
        frameDuration: Float = 0.1f,
        playMode: Animation.PlayMode = Animation.PlayMode.LOOP
    ) {
        val regions = atlas.findRegions(baseName)

        animations[name] = Animation(frameDuration, regions, playMode)
    }

    fun getController(currentAnimation: String) = AnimationController(this, currentAnimation)

    fun getAnimation(name: String): Animation<TextureRegion> {
        return animations[name] ?: throw IllegalArgumentException("Animation '$name' not found")
    }
}
