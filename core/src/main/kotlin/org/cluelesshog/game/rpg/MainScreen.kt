package org.cluelesshog.game.rpg

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.utils.Disposable
import engine.asset.MapAsset
import ktx.app.KtxScreen
import org.cluelesshog.game.GdxGame
import org.cluelesshog.game.rpg.system.RenderSystem
import kotlin.math.min

//class MainScreen(private val map: GameMap = MapLoader.load("maps/test_map.tmx")) : Scene(wrapper = MapStage(map)) {
//    override fun load(): Boolean {
//        val player = PlayerActor(map)
//        wrapper.addListener(MoveInputProcessor(player))
//
//        player.setSize(2f, 2f)
//        player.setPosition(1f, 2f)
//
//        wrapper.addActor(player)
//
//        return true
//    }
//}


class MainScreen() : KtxScreen {
    private val assetService = GdxGame.assetService
    private val camera = GdxGame.camera
    private val viewport = GdxGame.viewport
    private val batch = GdxGame.batch
    private val engine = Engine().apply {
        addSystem(RenderSystem(batch, viewport, assetService))
    }

    override fun show() {
        assetService.load(MapAsset.MAIN)
        engine.getSystem(RenderSystem::class.java).setMap(assetService.get(MapAsset.MAIN))
    }

    override fun render(delta: Float) {
        engine.update(
            min(delta, 1 / 30f)
        )

    }

    override fun hide() {
        engine.removeAllEntities()
    }

    override fun dispose() {
        for (system in engine.systems) {
            if (system is Disposable) {
                system.dispose()
            }
        }
    }
}
