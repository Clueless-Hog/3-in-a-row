package org.cluelesshog.engine.actor

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool
import com.badlogic.gdx.scenes.scene2d.Actor

class ParticleEffectActor(
    private val effect: ParticleEffectPool.PooledEffect
) : Actor() {

    override fun act(delta: Float) {
        super.act(delta)
        effect.update(delta)
        if (effect.isComplete) {
            effect.free() // return to pool
            remove()
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        effect.draw(batch)
    }

    override fun setPosition(x: Float, y: Float) {
        super.setPosition(x, y)
        effect.setPosition(x, y)
    }
}
