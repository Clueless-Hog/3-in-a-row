package org.cluelesshog.game.ai

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Image
import engine.AnimationController
import engine.AnimationPool
import engine.event.EventBus
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.logic.event.Match
import org.cluelesshog.game.scene.gamescreen.event.JewelClicked

class SwapBot(
    private val board: Board,
    private val isAllowedToAct: () -> Boolean
) : Image() {
    private val delay = 5f
    private var elapsed = 0f

    private val pool: AnimationPool
    private val controller: AnimationController

    init {
        val atlas = TextureAtlas(Gdx.files.internal("sprites/swap_helper.atlas"))

        pool = AnimationPool(atlas)
        pool.createAnimation("idle", "helper_sleep", frameDuration = .6f)
        pool.createAnimation("active", "helper_active")

        controller = pool.getController("idle")

        EventBus.subscribe<Match> {
            elapsed = 0f
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
        controller.update(delta)

        if (isAllowedToAct()) {
            elapsed += delta
        }

        EventBus.subscribe<JewelClicked> {
            controller.switchTo("idle")
            resetIdleTimer()
        }

        if (elapsed > delay) {
            SwapAI.randomSwap(board)

            controller.switchTo("active")
            resetIdleTimer()
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(
            controller.getCurrentFrame(),
            x, y,
            originX, originY,
            width, height,
            scaleX, scaleY,
            rotation
        )
    }

    private fun resetIdleTimer() {
        elapsed = 0f
    }
}
