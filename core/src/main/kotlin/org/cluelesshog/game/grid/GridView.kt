package org.cluelesshog.game.grid

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image

class GridView(private val grid: GridModel, private var cellSize: Float) {
    private val group = Group()

    init {
        group.setSize(grid.width * cellSize, grid.height * cellSize)
        buildGrid()
    }

    private fun buildGrid() {
        group.clearChildren()

        for ((pos, cell) in grid.getGrid()) {
            val (x, y) = pos

            val cellImage = Image(cell.texture.getDrawable())
            cellImage.setSize(cellSize, cellSize)
            cellImage.setPosition(x * cellSize, y * cellSize)

            group.addActor(cellImage)
        }
    }


    fun resize(newWidth: Int, newHeight: Int) {
        val padding = 50
        val availableWidth = newWidth - 2 * padding
        val availableHeight = newHeight - 2 * padding

        cellSize = minOf(availableWidth / grid.width, availableHeight / grid.height).toFloat()

        group.setSize(grid.width * cellSize, grid.height * cellSize)

        for (actor in group.children) {
            val x = (actor.x / actor.width).toInt()
            val y = (actor.y / actor.height).toInt()

            actor.setSize(cellSize, cellSize)
            actor.setPosition(x * cellSize, y * cellSize)
        }

        val centerX = padding + (availableWidth - group.width) / 2f
        val centerY = padding + (availableHeight - group.height) / 2f
        group.setPosition(centerX, centerY)
    }

    fun refresh() {
        buildGrid()
    }

    fun getActor(): Group = group

    fun dispose() {
        group.clear()
    }
}
