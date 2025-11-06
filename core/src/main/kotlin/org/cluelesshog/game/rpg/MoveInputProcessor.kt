package org.cluelesshog.game.rpg

import com.badlogic.gdx.Input.Keys.A
import com.badlogic.gdx.Input.Keys.D
import com.badlogic.gdx.Input.Keys.S
import com.badlogic.gdx.Input.Keys.W
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import ktx.actors.KtxInputListener

class MoveInputProcessor(private val player: PlayerActor) : KtxInputListener() {
    private val pressedKeys = mutableListOf<Int>()

    override fun keyDown(event: InputEvent, keycode: Int): Boolean {
        if (keycode !in pressedKeys)
            pressedKeys.add(keycode)
        updateMovement()
        return true
    }

    override fun keyUp(event: InputEvent, keycode: Int): Boolean {
        pressedKeys.remove(keycode)
        updateMovement()
        return true
    }

    private fun updateMovement() {
        var moveX = 0f
        var moveY = 0f

        when (pressedKeys.lastOrNull()) {
            W -> moveY = 1f
            S -> moveY = -1f
            A -> moveX = -1f
            D -> moveX = 1f
        }

        player.setMove(moveX, moveY)
    }
}

