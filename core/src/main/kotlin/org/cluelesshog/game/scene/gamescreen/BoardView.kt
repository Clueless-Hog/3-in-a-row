package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import ktx.actors.onClick
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.logic.Jewel
import org.cluelesshog.game.logic.JewelPos
import org.cluelesshog.game.logic.SwapResult

class BoardView(private val board: Board, private val scoreView: ScoreView, width: Float, height: Float) : Group() {
    private var jewelSize = minOf(width / board.columnsCount, height / board.rowsCount)
    private val actors = mutableMapOf<JewelPos, JewelActor>()
    private var previous: JewelActor? = null

    init {
        for (jewel in board) {
            val actor = getJewelImage(jewel)
            addActor(actor)
            actors[jewel.pos] = actor
        }

        val boardTop = board.rowsCount * jewelSize
        actors.values.forEach {
            val toX = it.x
            val toY = it.y

            it.y = boardTop + (jewelSize * it.getPos().row)
            it.addAction(Actions.moveTo(toX, toY, 2f, Interpolation.ExpOut(10f, 4f)))
        }
        setSize(board.columnsCount * jewelSize, board.rowsCount * jewelSize)
    }

    private fun refresh(result: List<SwapResult>) {
        result.forEach { res ->
            res.matches.forEach {
                actors[it]!!.addAction(
                    Actions.sequence(
                        Actions.fadeOut(.5f),
                        Actions.removeActor(),
                        Actions.run {
                            actors.remove(it)
                        }
                    )
                )
            }
        }
    }

    private fun getJewelImage(jewel: Jewel): JewelActor {
        val actor = JewelActor(jewel, jewelSize)

        actor.onClick {
            if (previous == null) {
                highlight()
                previous = this
                return@onClick
            }
            val prev = previous!!
            prev.unhighlight()

            val swap = board.swap(prev.getPos(), getPos())
            if (swap.isEmpty()) {
                highlight()
                previous = this
                return@onClick
            }
            actors[getPos()] = this
            actors[prev.getPos()] = prev

            swapActors(prev, this) {
                refresh(swap)
            }

            previous = null
        }

        return actor
    }

    private fun swapActors(first: JewelActor, second: JewelActor, onComplete: () -> Unit) {
        val firstPos = first.x to first.y
        val secondPos = second.x to second.y

        first.addAction(Actions.moveTo(secondPos.first, secondPos.second, 0.3f, Interpolation.ExpOut(2f, 3f)))

        second.addAction(
            Actions.sequence(
                Actions.moveTo(firstPos.first, firstPos.second, 0.3f, Interpolation.ExpOut(2f, 3f)),
                Actions.run { onComplete() }
            ))
    }
}
