package engine.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Disposable
import com.github.tommyettinger.freetypist.FreeTypistSkinLoader

class AssetService(fileHandleResolver: FileHandleResolver) : Disposable {
    private val assetManager = AssetManager(fileHandleResolver)

    init {
        assetManager.setLoader(TiledMap::class.java, TmxMapLoader())
        assetManager.setLoader(
            Skin::class.java,
            FreeTypistSkinLoader(assetManager.fileHandleResolver)
        )
    }

    fun <T> load(asset: Asset<T>): T {
        assetManager.load(asset.getDescriptor())
        assetManager.finishLoading()
        return assetManager.get(asset.getDescriptor())
    }

    fun <T> unload(asset: Asset<T>) {
        assetManager.unload(asset.getDescriptor().fileName)
    }

    fun <T> queue(asset: Asset<T>) {
        assetManager.load(asset.getDescriptor())
    }

    fun <T> get(asset: Asset<T>) = assetManager.get(asset.getDescriptor())!!

    fun update() = assetManager.update()

    fun debugDiagnostics() {
        Gdx.app.debug("AssetService", this.assetManager.getDiagnostics())
    }

    override fun dispose() {
        assetManager.dispose()
    }
}
