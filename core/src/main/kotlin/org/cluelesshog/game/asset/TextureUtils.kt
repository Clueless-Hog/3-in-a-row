package org.cluelesshog.game.asset

import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import engine.asset.AssetLoader.getTile
import engine.asset.AssetLoader.getAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

object TextureUtils {
    private val cache = mutableMapOf<String, TextureRegionDrawable>()

    fun loadTexture(texture: String): TextureRegionDrawable {
        return cache.getOrPut(texture) {
            TextureRegionDrawable(TextureRegion(getTile(texture)))
        }
    }

    fun loadTexture(texture: String, atlas: String): TextureRegionDrawable {
        val cacheKey = "$atlas:>$texture"

        return cache.getOrPut(cacheKey) {
            val atlas = getAtlas(atlas)

            TextureRegionDrawable(atlas.findRegion(texture))
        }
    }

    fun loadInvisibleTexture(): TextureRegionDrawable {
        return cache.getOrPut("invisible") {
            val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888)
            pixmap.setColor(0f, 0f, 0f, 0f)
            pixmap.fill()
            val texture = Texture(pixmap)
            pixmap.dispose()

            TextureRegionDrawable(TextureRegion(texture))
        }
    }
}
