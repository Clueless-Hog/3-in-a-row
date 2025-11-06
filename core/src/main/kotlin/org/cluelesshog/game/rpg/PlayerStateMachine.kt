package org.cluelesshog.game.rpg

class PlayerStateMachine(private val player: PlayerActor) {
    private var currentState: PlayerState = IdleState

    fun changeState(newState: PlayerState) {
        currentState.exit(player)
        currentState = newState
        currentState.enter(player)
    }

    fun handleInput(input: InputData) {
        currentState.handleInput(player, input)
    }

    fun update(delta: Float) {
        currentState.update(player, delta)
    }
}

object IdleState : PlayerState {
    override fun enter(player: PlayerActor) {
        player.setAnim(PlayerAnimationState.IDLE)
    }

    override fun handleInput(player: PlayerActor, input: InputData) {
        if (input.moveX != 0f || input.moveY != 0f) {
            player.stateMachine.changeState(WalkState)
        }
    }

    override fun update(player: PlayerActor, delta: Float) {}

    override fun exit(player: PlayerActor) {}
}

object WalkState : PlayerState {
    var temp = InputData()

    override fun enter(player: PlayerActor) {
        player.updateDirection()
        player.setAnim(PlayerAnimationState.WALK)
    }

    override fun handleInput(player: PlayerActor, input: InputData) {
        if (input.moveX == 0f && input.moveY == 0f) {
            player.stateMachine.changeState(IdleState)
            return
        }

        if (temp.moveX != input.moveX || temp.moveY != input.moveY) {
            temp = input.copy()
            player.updateDirection()
            player.setAnim(PlayerAnimationState.WALK)
        }
    }

    override fun update(player: PlayerActor, delta: Float) {
        player.tryMove(delta)
    }

    override fun exit(player: PlayerActor) {}
}
