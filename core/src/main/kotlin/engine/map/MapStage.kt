package engine.map

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import org.cluelesshog.game.settings.Settings

class MapStage(private val gameMap: GameMap) : Stage() {
    private val renderer = OrthogonalTiledMapRenderer(gameMap.map)
    private val camera = OrthographicCamera(
        gameMap.widthPx.toFloat(),
        gameMap.heightPx.toFloat()
    )
    private val viewport = FitViewport(
        Settings.resolution.width.toFloat(),
        Settings.resolution.height.toFloat()
    )

    init {
        camera.setToOrtho(false)
        camera.position.set(gameMap.widthPx / 2f, gameMap.heightPx / 2f, 0f)
        val zoomX = gameMap.widthPx / Gdx.graphics.width.toFloat()
        val zoomY = gameMap.heightPx / Gdx.graphics.height.toFloat()
        camera.zoom = maxOf(zoomX, zoomY, 1f)
    }

    override fun draw() {
        // сначала карта
//        viewport.apply()
//        camera.update()
//        val halfW = camera.viewportWidth / 2f
//        val halfH = camera.viewportHeight / 2f
//
//        camera.position.x = MathUtils.clamp(camera.position.x, halfW, gameMap.widthPx - halfW)
//        camera.position.y = MathUtils.clamp(camera.position.y, halfH, gameMap.heightPx - halfH)

        camera.update()
        renderer.setView(camera)
        renderer.render()

        // потом акторы
        super.draw()
    }
}
