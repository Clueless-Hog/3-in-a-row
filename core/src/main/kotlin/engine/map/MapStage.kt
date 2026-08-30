package engine.map

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport

class MapStage(
    gameMap: GameMap,
    camera: OrthographicCamera = OrthographicCamera()
) : Stage(FitViewport(30f, 20f, camera)) {
    private val renderer = OrthogonalTiledMapRenderer(gameMap.map)

    init {
        camera.setToOrtho(false)
        camera.position.set(gameMap.widthPx / 2f, gameMap.heightPx / 2f, 0f)
        camera.update()
        renderer.setView(camera)
    }

    override fun draw() {
        renderer.render()

        super.draw()
    }
}
