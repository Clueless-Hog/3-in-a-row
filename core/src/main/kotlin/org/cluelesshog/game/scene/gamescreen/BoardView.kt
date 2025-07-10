package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import ktx.actors.onClick
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.logic.Jewel

class BoardView(private val grid: Board, width: Float, height: Float) : Group() {
    private var jewelSize = minOf(width / grid.columns, height / grid.rows)
    private val actors = mutableMapOf<Int, JewelActor>()
    private var previousSelectedPos: Pair<Int, Int>? = null

    init {
        for ((pos, jewel) in grid.getBoard()) {
            val actor = getJewelImage(jewel)
            addActor(actor)
            actors[jewel.id] = actor
        }

        setSize(grid.columns * jewelSize, grid.rows * jewelSize)
    }

    private fun refresh() {
        actors.forEach { it.value.updatePosition() }

//        for ((pos, jewel) in grid.getBoard()) {
//            val actor = actors[pos]!!
//            actor.updatePosition()
//            actors[jewel.column to jewel.row] = actor
//        }
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
