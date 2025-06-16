package org.cluelesshog.game.grid

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import org.cluelesshog.game.asset.TextureUtils.createSolidTexture

enum class CellTexture(private val color: Color) {
    RED(Color.RED),
    GREEN(Color.GREEN),
    BLUE(Color.BLUE);

    private val texture: Texture by lazy {
        createSolidTexture(color, 1, 1)
    }

    fun getDrawable(): TextureRegionDrawable {
        return TextureRegionDrawable(TextureRegion(texture))
    }
}
