package org.cluelesshog.game.asset

import asset.AssetLoader
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import org.cluelesshog.game.logic.JewelType

object TextureUtils {
    private val jewelTextureCache = mutableMapOf<JewelType, TextureRegionDrawable>()

    fun loadTextureForJewelType(jewelType: JewelType): TextureRegionDrawable {
        return jewelTextureCache.getOrPut(jewelType) {
            val texture = when (jewelType) {
                JewelType.DIAMOND -> AssetLoader.getTile("diamond")
                JewelType.EMERALD -> AssetLoader.getTile("emerald")
                JewelType.RUBY -> AssetLoader.getTile("ruby")
                JewelType.AMETHYST -> AssetLoader.getTile("amethyst")
            }
            TextureRegionDrawable(TextureRegion(texture))
        }
    }
}
