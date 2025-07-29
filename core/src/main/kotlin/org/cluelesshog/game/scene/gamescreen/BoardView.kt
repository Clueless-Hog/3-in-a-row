package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.scenes.scene2d.Group
import ktx.actors.onClick
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.logic.Jewel
import org.cluelesshog.game.logic.JewelPos

class BoardView(private val board: Board, width: Float, height: Float) : Group() {
    private var jewelSize = minOf(width / board.columnsCount, height / board.rowsCount)
    private val actors = mutableMapOf<JewelPos, JewelActor>()
    private var previousSelectedPos: JewelPos? = null

    init {
        for (jewel in board) {
            val actor = getJewelImage(jewel)
            addActor(actor)
            actors[jewel.pos] = actor
        }

        setSize(board.columnsCount * jewelSize, board.rowsCount * jewelSize)
    }

    private fun refresh() {
        actors.forEach { it.value.update(board) }
    }

    private fun getJewelImage(jewel: Jewel): JewelActor {
        val actor = JewelActor(jewel, jewelSize)

        actor.onClick {
            if (previousSelectedPos == null) {
                highlight()
                previousSelectedPos = getPos()
            } else {
                val selectedJewel = board.getJewel(previousSelectedPos!!)
                actors[selectedJewel.pos]!!.unhighlight()

                if (board.swap(previousSelectedPos!!, getPos())) {
                    previousSelectedPos = null
                    refresh()
                } else {
                    highlight()
                    previousSelectedPos = getPos()
                }
            }
        }

        return actor
    }
}
