package org.cluelesshog.game.asset

import asset.AssetLoader.getTile
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

object TextureUtils {
    private val cache = mutableMapOf<String, TextureRegionDrawable>()

    fun loadTexture(texture: String): TextureRegionDrawable {
        return cache.getOrPut(texture) {
            TextureRegionDrawable(TextureRegion(getTile(texture)))
        }
    }
}
