package org.cluelesshog.game.asset

import org.cluelesshog.engine.asset.AssetLoader
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
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

    fun getHighlightTexture(original: Drawable): Drawable {
        if (original is TextureRegionDrawable) {
            val highlighted = TintableRegionDrawable(original.region)
            highlighted.setTint(Color.LIGHT_GRAY)
            return highlighted
        }
        return original
    }
}

class TintableRegionDrawable(textureRegion: TextureRegion) : TextureRegionDrawable(textureRegion) {
    private val tintColor = Color(1f, 1f, 1f, 1f)

    fun setTint(tint: Color) {
        tintColor.set(tint)
    }

    override fun draw(batch: Batch, x: Float, y: Float, width: Float, height: Float) {
        val prev = batch.color.cpy()
        batch.color = prev.mul(tintColor)
        super.draw(batch, x, y, width, height)
        batch.color = prev
    }
}
