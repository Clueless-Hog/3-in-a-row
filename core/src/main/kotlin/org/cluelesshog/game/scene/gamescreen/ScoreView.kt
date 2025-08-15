package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.scenes.scene2d.ui.Label
import org.cluelesshog.game.asset.Theme
import org.cluelesshog.game.logic.Board

class ScoreView(score: Int = 0)
    : Label("Score: $score", Theme.default()) {
    fun update(scoreUp: Int) {
        setScore(scoreUp)
    }

    private fun setScore(score: Int) {
        setText("Score: $score")
    }
}
