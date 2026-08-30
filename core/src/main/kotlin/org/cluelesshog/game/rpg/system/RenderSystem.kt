package org.cluelesshog.game.rpg.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.Viewport
import engine.asset.AssetService
import org.cluelesshog.game.GdxGame
import org.cluelesshog.game.rpg.component.Graphic
import org.cluelesshog.game.rpg.component.Transform

class RenderSystem(
    private val batch: Batch,
    private val viewport: Viewport,
    private val assetService: AssetService
) : SortedIteratingSystem(
    Family.all(Transform::class.java, Graphic::class.java).get(),
    Comparator.comparing(Transform.MAPPER::get)
), Disposable {
    private val mapRenderer = OrthogonalTiledMapRenderer(null, GdxGame.UNIT_SCALE, batch)
    private val camera = viewport.camera as OrthographicCamera

    fun setMap(map: TiledMap) {
        mapRenderer.setMap(map)
    }

    override fun update(deltaTime: Float) {
        viewport.apply()
        batch.setColor(Color.WHITE)
        mapRenderer.setView(camera)
        mapRenderer.render()

        forceSort()
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = Transform.MAPPER.get(entity)
        val graphic = Graphic.MAPPER.get(entity)

        if (graphic.region == null) {
            return
        }

        val position = transform.position
        val scaling = transform.scaling
        val size = transform.size

        batch.setColor(graphic.color)
        batch.draw(
            graphic.region,
            position.x - (1f - scaling.x) * size.x * 0.5f,
            position.y - (1f - scaling.y) * size.y * 0.5f,
            size.x * 0.5f, size.y * 0.5f,
            size.x, size.y,
            scaling.x, scaling.y,
            transform.rotationDeg
        )

    }

    override fun dispose() {
        mapRenderer.dispose()
    }
}
