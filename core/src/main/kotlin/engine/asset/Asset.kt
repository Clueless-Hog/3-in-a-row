package engine.asset

import com.badlogic.gdx.assets.AssetDescriptor

interface Asset<T> {
    fun getDescriptor(): AssetDescriptor<T>
}
