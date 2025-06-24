package org.cluelesshog.game.asset

import asset.AssetLoader
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import org.cluelesshog.game.logic.JewelType

object TextureUtils {
    fun loadTextureForJewelType(jewelType: JewelType): TextureRegionDrawable {
        val texture = when (jewelType) {
            JewelType.DIAMOND -> AssetLoader.getTile("diamond")
            JewelType.EMERALD -> AssetLoader.getTile("emerald")
            JewelType.RUBY -> AssetLoader.getTile("ruby")
        }

        return TextureRegionDrawable(TextureRegion(texture))
    }
}
