package org.cluelesshog.engine.actor

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import org.cluelesshog.engine.geometry.Position

open class SpritedActor(position: Position, private val texture: Sprite) : Actor() {

    init {
        setSize(texture.width, texture.height)
        setPosition(position.x.toFloat(), position.y.toFloat())
        texture.setOriginCenter()
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        texture.draw(batch)
    }

    override fun positionChanged() {
        super.positionChanged()

        texture.setPosition(x, y)
    }

    override fun sizeChanged() {
        super.sizeChanged()

        texture.setSize(width, height)
    }

    override fun rotationChanged() {
        super.rotationChanged()

        texture.rotation = rotation
    }

    fun getPosition() = Position(x.toInt(), y.toInt())

    private val bounds: Rectangle
        get() = Rectangle(x, y, width, height)

    fun collides(with: SpritedActor): Boolean {
        return bounds.overlaps(with.bounds)
    }
}
