package org.cluelesshog.game.rpg

interface PlayerState {
    fun enter(player: PlayerActor)
    fun handleInput(player: PlayerActor, input: InputData)
    fun update(player: PlayerActor, delta: Float)
    fun exit(player: PlayerActor)
}
