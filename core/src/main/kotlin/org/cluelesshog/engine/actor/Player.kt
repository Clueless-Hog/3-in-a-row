package org.cluelesshog.engine.actor

import org.cluelesshog.engine.geometry.Position
import org.cluelesshog.engine.input.Binding
import org.cluelesshog.engine.input.KeyBoard
import org.cluelesshog.towerdefence.Assets

class Player(initialPosition: Position) : SpritedActor(initialPosition, Assets.enemy) {
    private var movementSpeed = 200f

    override fun act(delta: Float) {
        super.act(delta)

        var dx = 0f
        var dy = 0f
        if (KeyBoard.isPressed(Binding.MOVE_RIGHT)) {
            dx += movementSpeed * delta
        }

        if (KeyBoard.isPressed(Binding.MOVE_LEFT)) {
            dx -= movementSpeed * delta
        }

        if (KeyBoard.isPressed(Binding.MOVE_UP)) {
            dy += movementSpeed * delta
        }

        if (KeyBoard.isPressed(Binding.MOVE_DOWN)) {
            dy -= movementSpeed * delta
        }

        if (dx != 0f || dy != 0f) {
            moveBy(dx, dy)
        }
    }
}
