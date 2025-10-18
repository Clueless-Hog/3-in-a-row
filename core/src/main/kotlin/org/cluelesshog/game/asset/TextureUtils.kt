package org.cluelesshog.game.asset

import asset.AssetLoader.getTile
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import org.cluelesshog.game.ai.SwapBotState
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

    fun loadTextureForHelperState(state: SwapBotState): TextureRegionDrawable {
        val texture = when (state) {
            SwapBotState.ACTIVE -> getTile("kek")
            SwapBotState.SLEEP -> getTile("zzz")
        }
        return TextureRegionDrawable(TextureRegion(texture))
    }
}
