package org.cluelesshog.game.scene.gamescreen

import org.cluelesshog.game.Scene
import org.cluelesshog.game.logic.Board

class GameScreen : Scene() {
    private lateinit var model: Board
    private lateinit var view: BoardView
    private val rows = 8
    private val columns = 8
    private val boardSize: Float
        get() = minOf(getScreenWidth() * 0.8f, getScreenHeight() * 0.8f)

    override fun dispose() {
        wrapper.dispose()
    }

    override fun load(): Boolean {
        model = Board(rows, columns)
        view = BoardView(model, boardSize, boardSize)

        view.setPosition(
            (getScreenWidth() - boardSize) / 2f,
            (getScreenHeight() - boardSize) / 2f
        )

        wrapper.addActor(view.getActor())

        return true
    }
}
