package org.cluelesshog.game.scene.gamescreen

import org.cluelesshog.game.Scene
import org.cluelesshog.game.logic.Board

class GameScreen : Scene() {
    private lateinit var model: Board
    private lateinit var view: BoardView
    private val rows = 8
    private val columns = 8
    private val boardWidth = 500
    private val boardHeight = 500

    override fun dispose() {
        wrapper.dispose()
    }

    override fun load(): Boolean {
        model = Board(rows, columns)
        view = BoardView(model, boardWidth, boardHeight)

        view.setPosition(
            (this.getScreenWidth() - boardWidth) / 2f,
            (this.getScreenHeight() - boardHeight) / 2f
        )

        wrapper.addActor(view.getActor())

        return true
    }
}
