package org.cluelesshog.game.ai

import com.badlogic.gdx.scenes.scene2d.ui.Image
import engine.event.EventBus
import org.cluelesshog.game.asset.TextureUtils.loadTextureForHelperState
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.logic.event.Match
import org.cluelesshog.game.scene.gamescreen.event.JewelClicked

class SwapBot(private val board: Board, private val isAllowedToAct: () -> Boolean) : Image() {
    companion object {
        const val DELAY = 5f
    }

    private var elapsed = 0f

    init {
        setIdleState()
        EventBus.subscribe<Match> {
            elapsed = 0f
        }
    }

    override fun act(delta: Float) {
        super.act(delta)
        if (isAllowedToAct()) {
            elapsed += delta
        }

        EventBus.subscribe<JewelClicked> {
            resetIdleTimer()
            setIdleState()
        }

        if (elapsed > DELAY) {
            SwapAI.randomSwap(board)
            setActiveState()
            resetIdleTimer()
        }
    }

    private fun resetIdleTimer() {
        elapsed = 0f
    }

    private fun setIdleState() {
        drawable = loadTextureForHelperState(SwapBotState.SLEEP)
    }

    private fun setActiveState() {
        drawable = loadTextureForHelperState(SwapBotState.ACTIVE)
    }
}

enum class SwapBotState {
    ACTIVE,
    SLEEP
}
