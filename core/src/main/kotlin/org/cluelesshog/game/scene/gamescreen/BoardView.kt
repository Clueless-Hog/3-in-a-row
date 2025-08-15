package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Touchable
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
    private val boardTop = board.rowsCount * jewelSize

    init {
        for (jewel in board) {
            val actor = getJewelImage(jewel)
            addActor(actor)
            actors[jewel.pos] = actor
        }

        actors.values.forEach {
            val toX = it.x
            val toY = it.y

            it.y = boardTop + (jewelSize * it.pos.row)
            it.addAction(Actions.moveTo(toX, toY, 2f, Interpolation.ExpOut(10f, 4f)))
        }
        setSize(board.columnsCount * jewelSize, board.rowsCount * jewelSize)
    }

    private fun getJewelActorOrNull(pos: JewelPos) = actors[pos]

    private fun getJewelActor(pos: JewelPos) = getJewelActorOrNull(pos)!!

    private fun refresh(result: List<SwapResult>) {
        val steps = mutableListOf<Action>()
        touchable = Touchable.disabled
        result.forEach { res ->
            steps += Actions.run {
                res.matches.forEach { pos ->
                    actors[pos]!!.addAction(
                        Actions.sequence(
                            Actions.fadeOut(.4f),
                            Actions.removeActor(),
                            Actions.run { actors.remove(pos) }
                        )
                    )
                }
            }
            steps += Actions.run {
                gravity(res.movedJewels)
            }
            steps += Actions.run {
                refill(res.newJewels)
            }
            steps += Actions.run { scoreView.update(res.scoreUp) }
            steps += Actions.delay(1f)
        }
        addAction(Actions.sequence(*steps.toTypedArray(), Actions.run {
            touchable = Touchable.enabled
        }))
    }

    fun validateBoard(grid: Map<JewelPos, JewelActor>): Boolean {
        var isValid = true
        for ((pos, jewel) in grid) {
            if (pos != jewel.pos) {
                println("❌ Mismatch: key=$pos but jewel.pos=${jewel.pos} (type=${jewel.jewel.type})")
                isValid = false
            }
        }
        if (isValid) {
            println("✅ Board is consistent: all JewelPos match Jewel.pos")
        }
        return isValid
    }


    private fun gravity(movedJewels: MutableMap<JewelPos, Int>) {
        movedJewels.forEach {
            val pos = it.key
            val step = it.value
            val newPos = JewelPos(pos.column, pos.row - step)
            val actor = getJewelActor(pos)
            actors[newPos] = actor
            actor.pos = newPos
            actors.remove(pos)

            actor.addAction(Actions.moveBy(0f, -(step * jewelSize), 2f, Interpolation.ExpOut(10f, 4f)))
        }
        validateBoard(actors)
    }

    private fun refill(newJewels: List<Jewel>) {
        newJewels.forEach { newJewel ->
            val newActor = getJewelImage(newJewel)
            actors[newJewel.pos] = newActor

            val toX = newActor.x
            val toY = newActor.y

            newActor.y = boardTop + (jewelSize * newActor.pos.row)
            addActor(newActor)
            newActor.addAction(Actions.moveTo(toX, toY, 2f, Interpolation.ExpOut(10f, 4f)))
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
            val swap = board.swap(prev.pos, pos)
            if (swap.isEmpty()) {
                highlight()
                previous = this
                return@onClick
            }

            val temp = prev.pos
            prev.pos = this.pos
            this.pos = temp

            actors[pos] = this
            actors[prev.pos] = prev

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
