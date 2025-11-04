package org.cluelesshog.game.ai

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Actor
import engine.AnimationController
import engine.AnimationPool
import engine.asset.AssetLoader
import engine.event.listen
import org.cluelesshog.game.match3.logic.Board
import org.cluelesshog.game.match3.logic.RNG
import org.cluelesshog.game.match3.logic.event.Match
import org.cluelesshog.game.match3.logic.event.JewelClicked

class SwapBot(
    private val board: Board,
    private val isAllowedToAct: () -> Boolean
) : Actor() {
    private val delay = 3f
    private var elapsed = 0f

    private val pool: AnimationPool
    private val controller: AnimationController

    init {
        val atlas = AssetLoader.getAtlas("swap_helper")

        pool = AnimationPool(atlas)
        pool.createAnimation("idle", "helper_sleep", frameDuration = .6f)
        pool.createAnimation("active", "helper_active")

        controller = pool.getController("idle")

        listen<Match> {
            resetIdleTimer()
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
        controller.update(delta)

        if (isAllowedToAct()) {
            elapsed += delta
        }

        listen<JewelClicked> {
            controller.switchTo("idle")
            resetIdleTimer()
        }

        if (elapsed > delay) {
            val move = board.getPossibleMoves().random(RNG.seed)
            board.swap(move.first, move.second)

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
