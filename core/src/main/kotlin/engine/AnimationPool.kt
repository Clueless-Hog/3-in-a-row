package engine

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor

class AnimationPool(private val atlas: TextureAtlas) {
    private val animations = HashMap<String, Animation<TextureRegion>>()
    var frameDuration = .1f

    fun createAnimation(
        name: String,
        baseName: String = name,
        frameDuration: Float = this.frameDuration,
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

//fun main() {
//    val enemy = Actor()
//
//    val controller = AnimationController("goblin", "idle")
//
//    enemy.controller = controller
//
//    controller.switchTo("atack")
//}
