package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.scenes.scene2d.Group
import ktx.actors.onClick
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.logic.Jewel
import org.cluelesshog.game.logic.JewelPos

class BoardView(private val grid: Board, width: Float, height: Float) : Group() {
    private var jewelSize = minOf(width / grid.columnsCount, height / grid.rowsCount)
    private val actors = mutableMapOf<Int, JewelActor>()
    private var previousSelectedPos: JewelPos? = null

    init {
        for ((pos, jewel) in grid.getBoard()) {
            val actor = getJewelImage(jewel)
            addActor(actor)
            actors[jewel.id] = actor
        }

        setSize(grid.columnsCount * jewelSize, grid.rowsCount * jewelSize)
    }

    private fun refresh() {
        actors.forEach { it.value.updatePosition() }
    }

    private fun getJewelImage(jewel: Jewel): JewelActor {
        val actor = JewelActor(jewel, jewelSize)

        actor.onClick {
            if (previousSelectedPos == null) {
                highlight()
                previousSelectedPos = getCoordinates()
            } else {
                val selectedJewel = grid.getJewel(previousSelectedPos!!)
                actors[selectedJewel.id]!!.unhighlight()

                if (grid.swap(previousSelectedPos!!, getCoordinates())) {
                    previousSelectedPos = null
                    refresh()
                } else {
                    highlight()
                    previousSelectedPos = getCoordinates()
                }
            }
        }

        return actor
    }
}
