package engine.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.g2d.TextureAtlas

enum class AtlasAsset(atlasName: String) : Asset<TextureAtlas> {
    OBJECTS("objects.atlas");

    private val descriptor = AssetDescriptor("graphics/$atlasName", TextureAtlas::class.java)

    override fun getDescriptor() = descriptor
}
