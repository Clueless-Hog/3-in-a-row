package engine.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture

enum class TextureAsset(path: String) : Asset<Texture> {
    EMERALD("emerald.png");

    private val descriptor = AssetDescriptor("tiles/$path", Texture::class.java)

    override fun getDescriptor() = descriptor
}
