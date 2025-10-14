package org.cluelesshog.game.asset

import asset.AssetLoader.getTile
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import org.cluelesshog.game.logic.JewelType

object TextureUtils {
    private val cache = mutableMapOf<String, TextureRegionDrawable>()

    fun loadTextureForJewelType(jewelType: JewelType): TextureRegionDrawable {
        return cache.getOrPut(jewelType.name) {
            val texture = when (jewelType) {
                JewelType.DIAMOND -> getTile("diamond")
                JewelType.EMERALD -> getTile("emerald")
                JewelType.RUBY -> getTile("ruby")
                JewelType.AMETHYST -> getTile("amethyst")
            }
            TextureRegionDrawable(TextureRegion(texture))
        }
    }

    fun loadTexture(texture: String): TextureRegionDrawable {
        return cache.getOrPut(texture) {
            TextureRegionDrawable(TextureRegion( AssetLoader.getTile(texture)))
        }
    }
}
