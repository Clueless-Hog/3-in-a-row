package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.scenes.scene2d.Group
import ktx.actors.onClick
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.logic.Jewel
import org.cluelesshog.game.logic.JewelPos
import org.cluelesshog.game.logic.MatchDetect

class BoardView(private val board: Board, width: Float, height: Float) : Group() {
    private var jewelSize = minOf(width / board.columnsCount, height / board.rowsCount)
    private val actors = mutableMapOf<Int, JewelActor>()
    private var previousSelectedPos: JewelPos? = null

    init {
        for (jewel in board) {
            val actor = getJewelImage(jewel)
            addActor(actor)
            actors[jewel.id] = actor
        }

        setSize(board.columnsCount * jewelSize, board.rowsCount * jewelSize)
    }

    private fun refresh() {
        val matches = MatchDetect.detect(board)
        matches.forEach { println(it) }
        actors.forEach { it.value.updatePosition() }
        println()
    }

    private fun getJewelImage(jewel: Jewel): JewelActor {
        val actor = JewelActor(jewel, jewelSize)

        actor.onClick {
            if (previousSelectedPos == null) {
                highlight()
                previousSelectedPos = getCoordinates()
            } else {
                val selectedJewel = board.getJewel(previousSelectedPos!!)
                actors[selectedJewel.id]!!.unhighlight()

                if (board.swap(previousSelectedPos!!, getCoordinates())) {
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
