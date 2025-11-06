package org.cluelesshog.game.rpg

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Actor
import engine.AnimationController
import engine.AnimationPool
import engine.map.CollisionSystem
import engine.map.GameMap
import org.cluelesshog.game.rpg.Direction.*
import kotlin.math.abs

class PlayerActor(
    private val gameMap: GameMap,
    private val collisionSystem: CollisionSystem = CollisionSystem(gameMap)
) : Actor() {
    var speed = 200f
    private var direction = DOWN

    private var pool: AnimationPool
    var controller: AnimationController

    val stateMachine = PlayerStateMachine(this)
    var inputData = InputData()

    init {
        val atlas = TextureAtlas(Gdx.files.internal("sprites/Swordsman.atlas"))
        pool = AnimationPool(atlas)

        pool.apply {
            frameDuration = .09f

            createAnimation("idle_up")
            createAnimation("idle_down")
            createAnimation("idle_left")
            createAnimation("idle_right")

            createAnimation("walk_up")
            createAnimation("walk_down")
            createAnimation("walk_left")
            createAnimation("walk_right")
        }

        controller = pool.getController("idle_down")
    }

    override fun act(delta: Float) {
        super.act(delta)
        controller.update(delta)

        stateMachine.handleInput(inputData)
        stateMachine.update(delta)
    }

    fun setAnim(state: PlayerAnimationState) {
        when(state) {
            PlayerAnimationState.WALK -> setAnimByDirection("walk")
            PlayerAnimationState.IDLE -> setAnimByDirection("idle")
        }
    }

    fun updateDirection() {
        val dx = inputData.moveX
        val dy = inputData.moveY

        direction = when {
            dy > 0 -> UP
            dy < 0 -> DOWN
            dx < 0 -> LEFT
            dx > 0 -> RIGHT
            else -> direction
        }
    }

    fun tryMove(delta: Float) {
        val dx = inputData.moveX * speed * delta
        val dy = inputData.moveY * speed * delta
        val nextRect = Rectangle(x + dx, y + dy, width, height)

        if (!collisionSystem.isBlocked(nextRect)) {
            moveBy(dx, dy)
        }
    }

    override fun draw(batch: Batch, parentAlpha: Float) {
        batch.draw(
            controller.getCurrentFrame(),
            x, y,
            originX, originY,
            width, height,
            scaleX, scaleY,
            rotation
        )
    }

    private fun setAnimByDirection(anim: String) {
        when(direction) {
            UP -> controller.switchTo("${anim}_up")
            DOWN -> controller.switchTo("${anim}_down")
            RIGHT -> controller.switchTo("${anim}_right")
            LEFT -> controller.switchTo("${anim}_left")
        }
    }

    fun setMove(moveX: Float, moveY: Float) {
        inputData.moveX = moveX
        inputData.moveY = moveY
    }
}

enum class PlayerAnimationState {
    WALK, IDLE
}

enum class Direction {
    UP, DOWN, RIGHT, LEFT
}
