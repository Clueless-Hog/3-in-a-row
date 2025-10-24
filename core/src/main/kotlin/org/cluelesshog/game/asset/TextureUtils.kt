package org.cluelesshog.game.asset

import asset.AssetLoader.getTile
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import org.cluelesshog.game.logic.JewelType

object TextureUtils {
    private val jewelTextureCache = mutableMapOf<JewelType, TextureRegionDrawable>()

    fun loadTextureForJewelType(jewelType: JewelType): TextureRegionDrawable {
        return jewelTextureCache.getOrPut(jewelType) {
            val texture = when (jewelType) {
                JewelType.DIAMOND -> getTile("diamond")
                JewelType.EMERALD -> getTile("emerald")
                JewelType.RUBY -> getTile("ruby")
                JewelType.AMETHYST -> getTile("amethyst")
            }
            TextureRegionDrawable(TextureRegion(texture))
        }
    }
}
