package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.scenes.scene2d.ui.Label
import org.cluelesshog.game.asset.Theme
import org.cluelesshog.game.logic.Board

class ScoreView(private val board: Board, score: Int = 0)
    : Label("Score: $score", Theme.default()) {
    fun update() {
        setScore(board.getScore())
    }

    private fun setScore(score: Int) {
        setText("Score: $score")
    }
}
