package org.cluelesshog.game.scene.gamescreen

import org.cluelesshog.game.Scene
import org.cluelesshog.game.logic.Board

class GameScreen : Scene() {
    private lateinit var model: Board
    private lateinit var view: BoardView
    private val width = 8
    private val height = 8
    private var jewelSize = 32f

    override fun dispose() {
        wrapper.dispose()
    }

    override fun load(): Boolean {
        model = Board(width, height)
        view = BoardView(model, jewelSize)

        wrapper.addActor(view.getActor())

        return true
    }
}
