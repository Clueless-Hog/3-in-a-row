package org.cluelesshog.game.rpg.component

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import engine.asset.AtlasAsset
import org.cluelesshog.game.rpg.component.Facing.*
import java.util.*

class Animation2D(
    val atlasAsset: AtlasAsset,
    val atlasKey: String,
    val type: AnimationType = AnimationType.IDLE,
    val playMode: Animation.PlayMode = Animation.PlayMode.NORMAL,
    val speed: Float = 1f,
) : Component {
    companion object {
        val MAPPER = ComponentMapper.getFor<Animation2D>(Animation2D::class.java)!!
    }

    lateinit var animation: Animation<TextureRegion>
    lateinit var direction: FacingDirection
    var stateTime: Float = 0f

    var isDirty: Boolean = true

    fun setAnimation(newAnimation: Animation<TextureRegion>, newDirection: FacingDirection) {
        animation = newAnimation
        stateTime = 0f
        direction = newDirection
        isDirty = false
    }

    fun incAndGetStateTime(deltaTime: Float): Float {
        stateTime += deltaTime * speed
        return stateTime
    }

    val isFinished: Boolean
        get() = animation.isAnimationFinished(stateTime)

    enum class AnimationType {
        IDLE, WALK, ATTACK, DAMAGED;
        val atlasKey: String = this.name.lowercase(Locale.getDefault())
    }
}
