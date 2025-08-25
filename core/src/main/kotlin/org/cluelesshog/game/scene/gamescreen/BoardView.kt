package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import ktx.actors.onClick
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.logic.Jewel
import org.cluelesshog.game.logic.JewelPos
import org.cluelesshog.game.logic.SwapResult
import kotlin.collections.forEach

class BoardView(private val board: Board, private val scoreView: ScoreView, width: Float, height: Float) : Group() {
    private var jewelSize = minOf(width / board.columnsCount, height / board.rowsCount)
    private val actors = mutableMapOf<JewelPos, JewelActor>()
    private var previous: JewelActor? = null
    private val boardTop = board.rowsCount * jewelSize

    init {
        touchable = Touchable.disabled
        for (jewel in board) {
            val actor = getJewelImage(jewel)
            addActor(actor)
            actors[jewel.pos] = actor
        }

        val initTrigger = ThresholdTrigger(board.count()) {
            touchable = Touchable.enabled
        }
        actors.values.forEach {
            val toX = it.x
            val toY = it.y

            it.y = boardTop + (jewelSize * it.pos.row)
            it.addAction(
                Actions.sequence(
                    Actions.moveTo(toX, toY, 2f, Interpolation.exp10Out),
                    Actions.run {
                        initTrigger.attempt()
                    }
                )
            )
        }
        setSize(board.columnsCount * jewelSize, board.rowsCount * jewelSize)
    }

    private fun gravityAndRefill(movedJewels: MutableMap<JewelPos, Int>, newJewels: List<Jewel>, onRefill: () -> Unit) {
        val afterGravityAndRefillTrigger = ThresholdTrigger(newJewels.size + movedJewels.size, onRefill)

        for ((pos, step) in movedJewels) {
            val newPos = JewelPos(pos.column, pos.row - step)
            val actor = actors[pos]!!
            actors[newPos] = actor
            actor.pos = newPos
            actors.remove(pos)

            actor.addAction(
                Actions.sequence(
                    Actions.moveBy(0f, -(step * jewelSize), .7f, Interpolation.exp10Out),
                    Actions.run {
                        afterGravityAndRefillTrigger.attempt()
                    }
                )
            )
        }

        for (it in newJewels) {
            val newActor = getJewelImage(it)
            actors[it.pos] = newActor

            val toX = newActor.x
            val toY = newActor.y

            newActor.y = boardTop + (jewelSize * newActor.pos.row)
            addActor(newActor)
            newActor.addAction(
                Actions.sequence(
                    Actions.moveTo(toX, toY, .7f, Interpolation.exp10Out),
                    Actions.run {
                        afterGravityAndRefillTrigger.attempt()
                    }
                )
            )
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

            val result = board.swap(prev.pos, pos)
            if (result.isEmpty()) {
                highlight()
                previous = this
                return@onClick
            }

            swapActors(prev, this, result)

            previous = null
        }

        return actor
    }

    private fun swapActors(first: JewelActor, second: JewelActor, swap: List<SwapResult>) {
        val temp = first.pos
        first.pos = second.pos
        second.pos = temp

        actors[first.pos] = first
        actors[second.pos] = second

        val firstPos = first.x to first.y
        val secondPos = second.x to second.y

        first.addAction(Actions.moveTo(secondPos.first, secondPos.second, 0.3f, Interpolation.exp10Out))

        second.addAction(
            Actions.sequence(
                Actions.moveTo(firstPos.first, firstPos.second, 0.3f, Interpolation.exp10Out),
                Actions.run { onMatch(ArrayDeque(swap)) }
            )
        )
    }

    private fun onMatch(swap: ArrayDeque<SwapResult>) {
        val combination = swap.removeFirstOrNull() ?: return

        touchable = Touchable.disabled
        val trigger = ThresholdTrigger(combination.matches.size) {
            scoreView.update(combination.scoreUp)

            gravityAndRefill(combination.movedJewels, combination.newJewels) {
                onMatch(swap)
                touchable = Touchable.enabled
            }
        }

        combination.matches.forEach { pos ->
            val actor = actors[pos]!!
            actors.remove(pos)
            actor.addAction(
                Actions.sequence(
                    Actions.scaleBy(.1f, .1f, .1f),
                    Actions.scaleBy(-1f, -1f, .2f),
                    Actions.fadeOut(.1f),
                    Actions.run {
                        trigger.attempt()
                        actor.remove()
                    },
                )
            )
        }

    }
}

data class ThresholdTrigger(private val threshold: Int, private val callback: () -> Unit) {
    private var attempts = 0
    fun attempt() {
        if (++attempts == threshold) {
            callback()
        }
    }
}
