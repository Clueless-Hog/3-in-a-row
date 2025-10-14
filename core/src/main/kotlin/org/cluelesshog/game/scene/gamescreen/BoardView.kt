package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import engine.Pipe
import engine.ThresholdTrigger
import engine.event.EventBus
import ktx.actors.onClick
import org.cluelesshog.game.asset.SoundManager
import org.cluelesshog.game.asset.SoundType
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.logic.Jewel
import org.cluelesshog.game.logic.JewelPos
import org.cluelesshog.game.logic.event.Match
import org.cluelesshog.game.logic.event.JewelSwapped
import org.cluelesshog.game.scene.gamescreen.event.JewelClicked
import kotlin.to

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

    private var pipe = Pipe()

    init {
        disableInput()
        for (jewel in board) {
            val actor = getJewelImage(jewel)
            addActor(actor)
            actors[jewel.pos] = actor
        }

        initAnimation()

        EventBus.subscribe<JewelSwapped> {
            currentCombo = 1f

            val first = actors[it.from]!!
            val second = actors[it.to]!!

            onSwap(first, second)
        }

        EventBus.subscribe<Match> {
            pipe.blocking {
                onMatch(it, pipe::unlock)
            }
        }

        setSize(board.columnsCount * jewelSize, board.rowsCount * jewelSize)

        pipe.run()
    }

    fun isLocked() = pipe.isLocked

    private fun initAnimation() {
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
    }

    private fun getJewelImage(jewel: Jewel): JewelActor {
        val actor = JewelActor(jewel, jewelSize)

        actor.onClick(::clickOnJewel)

        return actor
    }

    private fun clickOnJewel(actor: JewelActor) {
        EventBus.post(JewelClicked())

        if (previous == null) {
            actor.highlight()
            previous = actor

            return
        }

        val from = previous!!
        from.unhighlight()

        if (!board.swap(from.pos, actor.pos)) {
            actor.highlight()
            previous = actor
        }
    }

    private fun onSwap(first: JewelActor, second: JewelActor) {
        pipe.blocking {
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
                        pipe.unlock()
                    }
                )
            )
        }
    }

    private fun onMatch(match: Match, onComplete: () -> Unit) {
        disableInput()
        val completionTrigger = ThresholdTrigger(match.matches.size) {
            onJewelsDestroyed(match) {
                enableInput()
                onComplete()
            }
        }
        currentCombo += 0.3f
        // Удаление всех совпавших камней
        SoundManager.playSound(SoundType.MATCH, pitch = currentCombo)
        match.matches.forEach { (pos) ->
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

    private fun disableInput() {
        touchable = Touchable.disabled
    }

    private fun enableInput() {
        touchable = Touchable.enabled
    }
}
