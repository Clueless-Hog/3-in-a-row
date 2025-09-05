package org.cluelesshog.game.scene.gamescreen

import ktx.actors.centerPosition
import org.cluelesshog.game.Scene
import org.cluelesshog.game.logic.Board

class GameScreen : Scene() {
    private lateinit var model: Board
    private lateinit var view: BoardView
    private lateinit var score: ScoreView
    private val rows = 8
    private val columns = 8
    private val boardSize: Float
        get() = minOf(getScreenWidth() * 0.8f, getScreenHeight() * 0.8f)

    override fun dispose() {
        wrapper.dispose()
    }

    override fun load(): Boolean {
        model = Board(rows, columns)
        score = ScoreView()
        view = BoardView(model, score, boardSize, boardSize)

        wrapper.addActor(view)
        wrapper.addActor(score)
        score.setPosition(getScreenWidth() * 0.8f, getScreenHeight() * 0.9f)
        view.centerPosition()

        return true
    }
}
