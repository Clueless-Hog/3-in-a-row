package org.cluelesshog.game.rpg.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.GdxRuntimeException
import engine.asset.AssetService
import engine.asset.AtlasAsset
import org.cluelesshog.game.rpg.component.Animation2D
import org.cluelesshog.game.rpg.component.Facing
import org.cluelesshog.game.rpg.component.Graphic

class AnimationSystem(private val assetService: AssetService) :
    IteratingSystem(Family.all(
        Animation2D::class.java,
        Graphic::class.java,
        Facing::class.java
    ).get()) {
    companion object {
        private const val FRAME_DURATION = 1 / 8f
    }

    private val animationCache = HashMap<CacheKey, Animation<TextureRegion>>()

    /**
     * Updates animation state and sets graphic's component region.
     */
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val animation2D = Animation2D.MAPPER.get(entity)
        val facingDirection = Facing.MAPPER.get(entity).direction
        val stateTime: Float

        if (animation2D.isDirty || facingDirection !== animation2D.direction) {
            updateAnimation(animation2D, facingDirection)
            stateTime = 0f
        } else {
            stateTime = animation2D.incAndGetStateTime(deltaTime)
        }

        val animation = animation2D.animation
        animation.playMode = animation2D.playMode

        Graphic.MAPPER.get(entity).region = animation.getKeyFrame(stateTime)
    }

    /**
     * Updates animation based on direction and type, using cached animations.
     */
    private fun updateAnimation(animation2D: Animation2D, direction: Facing.FacingDirection) {
        val atlasAsset = animation2D.atlasAsset
        val atlasKey = animation2D.atlasKey
        val type = animation2D.type

        val cacheKey = CacheKey(atlasAsset, atlasKey, type, direction)

        val animation = animationCache.computeIfAbsent(cacheKey) { key ->
            val textureAtlas = assetService.get(atlasAsset)
            val combinedKey = atlasKey + "/" + type.atlasKey + "_" + direction.atlasKey
            val regions = textureAtlas.findRegions(combinedKey)

            if (regions.isEmpty) {
                throw GdxRuntimeException("No regions found for $key")
            }

            Animation<TextureRegion>(FRAME_DURATION, regions)
        }

        animation2D.setAnimation(animation, direction)
    }

    private data class CacheKey(
        val atlasAsset: AtlasAsset,
        val atlasKey: String,
        val type: Animation2D.AnimationType,
        val direction: Facing.FacingDirection
    )
}
