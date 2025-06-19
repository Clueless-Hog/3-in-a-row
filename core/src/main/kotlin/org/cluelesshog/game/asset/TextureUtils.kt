package org.cluelesshog.game.asset

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import org.cluelesshog.game.logic.JewelType

object TextureUtils {
    private val cache = HashMap<Color, Texture>()

    private fun createSolidTexture(color: Color, width: Int = 1, height: Int = 1): Texture {
        return cache.getOrPut(color) {
            val pixmap = Pixmap(width, height, Pixmap.Format.RGBA8888)
            pixmap.setColor(color)
            pixmap.fill()
            val texture = Texture(pixmap)
            pixmap.dispose()
            return texture
        }
    }

    fun loadTextureForJewelType(jewelType: JewelType): TextureRegionDrawable {
        val color = when (jewelType) {
            JewelType.DIAMOND -> Color.WHITE
            JewelType.EMERALD -> Color.GREEN
            JewelType.RUBY -> Color.RED
        }

        val texture: Texture = createSolidTexture(color)
        return TextureRegionDrawable(TextureRegion(texture))
    }
}
