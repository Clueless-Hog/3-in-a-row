package engine

import com.badlogic.gdx.graphics.g2d.TextureRegion

class AnimationController(
    private val pool: AnimationPool,
    private var currentAnimation: String
) {
    private var stateTime: Float = 0f

    fun switchTo(animationName: String, resetTime: Boolean = true) {
        currentAnimation = animationName
        if (resetTime) reset()
    }

    fun update(deltaTime: Float) {
        stateTime += deltaTime
    }

    fun reset() {
        stateTime = 0f
    }

    fun getCurrentFrame(): TextureRegion? = getCurrentAnimation().getKeyFrame(stateTime)

    private fun getCurrentAnimation() = pool.getAnimation(currentAnimation)
}
