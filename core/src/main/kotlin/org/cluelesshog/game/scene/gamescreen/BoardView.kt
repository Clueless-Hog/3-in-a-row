package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import engine.ThresholdTrigger
import ktx.actors.onClick
import org.cluelesshog.game.asset.SoundManager
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.logic.Jewel
import org.cluelesshog.game.logic.JewelPos
import org.cluelesshog.game.logic.Match

class BoardView(
    private val board: Board,
    private val scoreView: ScoreView,
    width: Float,
    height: Float
) : Group() {
    private var jewelSize = minOf(width / board.columnsCount, height / board.rowsCount)
    private val actors = mutableMapOf<JewelPos, JewelActor>()
    private var previous: JewelActor? = null
    private val boardTop = board.rowsCount * jewelSize
    private var currentCombo = 1f

    init {
        disableInput()
        for (jewel in board) {
            val actor = getJewelImage(jewel)
            addActor(actor)
            actors[jewel.pos] = actor
        }

        // Анимация падения камней, во время которой заблокирован ввод
        val initTrigger = ThresholdTrigger(board.count()) {
            enableInput()
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

    fun resize(newWidth: Float, newHeight: Float) {
        setSize(newWidth, newHeight)
        jewelSize = minOf(width / board.columnsCount, height / board.rowsCount)
        children.forEach {
            val actor = it as JewelActor
            actor.resize(jewelSize)
        }
    }

    private fun applyGravity(match: Match, onComplete: () -> Unit) {
        val movedJewels = match.movedJewels
        val newJewels = match.newJewels

        val afterGravityTrigger = ThresholdTrigger(newJewels.size + movedJewels.size) {
            if (match.refreshed) {
                refreshBoard(onComplete)
            } else {
                onComplete()
            }
        }

        // Гравитация
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
                        afterGravityTrigger.attempt()
                    }
                )
            )
        }

        // Добавление новых камней на место упавших
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
                        afterGravityTrigger.attempt()
                    }
                ),
            )
        }
    }

    private fun refreshBoard(onComplete: () -> Unit) {
        val clearActorsTrigger = ThresholdTrigger(actors.size) {
            // Заполнение доски новыми камнями + анимация
            board.forEach {
                val newActor = getJewelImage(it)
                newActor.scaleBy(-1f, -1f)
                addActor(newActor)
                actors[it.pos] = newActor

                newActor.addAction(
                    Actions.sequence(
                        Actions.scaleBy(1.1f, 1.1f, .2f),
                        Actions.scaleBy(-.1f, -.1f, .1f),
                        Actions.run(onComplete)
                    )
                )
            }
        }

        SoundManager.playSound(SoundType.MATCH)

        // Удаление всех камней + анимация
        actors.values.forEach {
            it.addAction(
                Actions.sequence(
                    Actions.scaleBy(.1f, .1f, .1f),
                    Actions.scaleBy(-1f, -1f, .2f),
                    Actions.fadeOut(.1f),
                    Actions.run {
                        clearActorsTrigger.attempt()
                        it.remove()
                    }
                )
            )
        }
    }

    private fun getJewelImage(jewel: Jewel): JewelActor {
        val actor = JewelActor(jewel, jewelSize)

        actor.onClick { clickOnJewel(this) }

        return actor
    }

    private fun clickOnJewel(second: JewelActor) {
        if (previous == null) {
            second.highlight()
            previous = second

            return
        }

        val first = previous!!
        first.unhighlight()
        val result = ArrayDeque(board.swap(first.pos, second.pos))
        if (result.isEmpty()) {
            second.highlight()
            previous = second

            return
        }

        previous = null

        val temp = first.pos
        first.pos = second.pos
        second.pos = temp

        actors[first.pos] = first
        actors[second.pos] = second

        val firstPos = first.x to first.y
        val secondPos = second.x to second.y

        disableInput()

        SoundManager.playSound(SoundType.SWAP)

        // Анимация свапа
        first.addAction(
            Actions.moveTo(
                secondPos.first,
                secondPos.second,
                0.3f,
                Interpolation.exp10Out
            )
        )

        second.addAction(
            Actions.sequence(
                Actions.moveTo(firstPos.first, firstPos.second, 0.3f, Interpolation.exp10Out),
                Actions.run {
                    enableInput()
                    handleMatches(result)
                }
            )
        )
    }

    private fun handleMatches(matches: ArrayDeque<Match>) {
        val match = matches.removeFirstOrNull()
        if (match == null) {
            currentCombo = 1f
            return
        }

        disableInput()

        onMatch(match) {
            enableInput()
            handleMatches(matches)
        }
    }

    private fun onMatch(match: Match, onComplete: () -> Unit) {
        val completionTrigger = ThresholdTrigger(match.matches.size) {
            onJewelsDestroyed(match, onComplete)
        }
        currentCombo += 0.3f
        // Удаление всех совпавших камней
        SoundManager.playSound(SoundType.MATCH, pitch = currentCombo)
        match.matches.forEach { pos ->
            val actor = actors[pos]!!
            actors.remove(pos)
            actor.addAction(
                Actions.sequence(
                    Actions.scaleBy(.1f, .1f, .1f),
                    Actions.scaleBy(-1f, -1f, .2f),
                    Actions.fadeOut(.1f),
                    Actions.run {
                        completionTrigger.attempt()
                        actor.remove()
                    },
                ),
            )
        }
    }

    private fun onJewelsDestroyed(match: Match, onComplete: () -> Unit) {
        scoreView.update(match.scoreUp)

        applyGravity(match, onComplete)
    }

    private fun disableInput() {
        touchable = Touchable.disabled
    }

    private fun enableInput() {
        touchable = Touchable.enabled
    }
}
