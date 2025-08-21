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
import kotlin.collections.forEach

class BoardView(private val board: Board, private val scoreView: ScoreView, width: Float, height: Float) : Group() {
    private var jewelSize = minOf(width / board.columnsCount, height / board.rowsCount)
    private val actors = mutableMapOf<JewelPos, JewelActor>()
    private var previous: JewelActor? = null
    private val boardTop = board.rowsCount * jewelSize
    private var destroyed = 0

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

    private fun getJewelActor(pos: JewelPos) = actors[pos]!!

    private fun refresh(result: List<SwapResult>) {
//        touchable = Touchable.disabled
        destroyed = 0
        handleSwap(result.first(), result)

//        sequence!!.addAction(Actions.run { touchable = Touchable.enabled })
//        addAction(sequence)
    }

    private fun handleSwap(result: SwapResult, result1: List<SwapResult>) {
        result.matches.forEach { pos ->
            val actor = getJewelActor(pos)
            actors.remove(pos)
            actor.addAction(
                Actions.sequence(
                    Actions.fadeOut(.1f),
                    Actions.removeActor(),
                    Actions.run {
                        onBlockDestroyed(result)
                    }
                )
            )
        }
    }

    private fun onBlockDestroyed(res: SwapResult) {
        if (res.matches.size == ++destroyed) {
            gravityAndRefill(res.movedJewels, res.newJewels)
            scoreView.update(res.scoreUp)
        }
    }

    private fun gravityAndRefill(movedJewels: MutableMap<JewelPos, Int>, newJewels: List<Jewel>) {
        var counter = Counter()
        movedJewels.forEach {
            val pos = it.key
            val step = it.value
            val newPos = JewelPos(pos.column, pos.row - step)
            val actor = getJewelActor(pos)
            actors[newPos] = actor
            actor.pos = newPos
            actors.remove(pos)

            actor.addAction(
                Actions.sequence(
                    Actions.moveBy(0f, -(step * jewelSize), .7f, Interpolation.exp10Out),
                    counter
                )
            )
        }
//        while (!counter.reached(movedJewels.size)) {
//        }

        counter = Counter()
        newJewels.forEach {
            val newActor = getJewelImage(it)
            actors[it.pos] = newActor

            val toX = newActor.x
            val toY = newActor.y

            newActor.y = boardTop + (jewelSize * newActor.pos.row)
            addActor(newActor)
            newActor.addAction(Actions.moveTo(toX, toY, .7f, Interpolation.exp10Out))
        }
//        while (!counter.reached(newJewels.size)) {
//        }
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

    private fun swapActors(first: JewelActor, second: JewelActor, onSwapFinished: () -> Unit) {
        val firstPos = first.x to first.y
        val secondPos = second.x to second.y

        first.addAction(Actions.moveTo(secondPos.first, secondPos.second, 0.3f, Interpolation.ExpOut(2f, 3f)))

        second.addAction(
            Actions.sequence(
                Actions.moveTo(firstPos.first, firstPos.second, 0.3f, Interpolation.ExpOut(2f, 3f)),
                Actions.run { onSwapFinished() }
            ))
    }
}

class Counter : Action() {
    private var counter: Int = 0

    fun reached(amount: Int): Boolean {
        Thread.sleep(100)
//        println("$counter: $amount")
        return counter == amount
    }

    override fun act(delta: Float): Boolean {
        ++counter
        return true
    }
}
