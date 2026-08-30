package org.cluelesshog.game

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import engine.SceneController
import engine.asset.AssetService
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import org.cluelesshog.game.mainMenu.MainMenuScreen

object GdxGame : KtxGame<KtxScreen>() {
    const val WORLD_WIDTH = 16f
    const val WORLD_HEIGHT = 9f
    const val UNIT_SCALE = 1f / 16f

    lateinit var batch: Batch
        private set

    lateinit var camera: OrthographicCamera
        private set

    lateinit var viewport: Viewport
        private set

    val assetService = AssetService(InternalFileHandleResolver())

    override fun create() {
        KtxAsync.initiate()

        batch = SpriteBatch()
        camera = OrthographicCamera()
        viewport = FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera)

        SceneController.display = this
        SceneController.set<MainMenuScreen>()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        super.resize(width, height)
    }

}
