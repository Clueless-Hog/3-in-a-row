package org.cluelesshog.game

import org.cluelesshog.game.grid.GridModel
import org.cluelesshog.game.grid.GridView

class GameScreen : Scene() {
    private lateinit var model: GridModel
    private lateinit var view: GridView
    private val width = 8
    private val height = 8
    private var cellSize = 32f

    override fun dispose() {
        wrapper.dispose()
    }

    override fun load(): Boolean {
        fullRedraw(width, height, cellSize)

        return true
    }

    override fun onResize(newWidth: Int, newHeight: Int) {
        super.onResize(newWidth, newHeight)
        view.resize(newWidth, newHeight)
    }

    fun fullRedraw(width: Int, height: Int, cellSize: Float) {
        model = GridModel(width, height)
        view = GridView(model, cellSize)

        wrapper.addActor(view.getActor())
        view.refresh()
    }
}
