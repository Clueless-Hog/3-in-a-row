package org.cluelesshog.engine.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

object KeyBoard {
    private val keyMap: MutableMap<Binding, Int> = mutableMapOf(
        Binding.MOVE_LEFT to Input.Keys.A,
        Binding.MOVE_RIGHT to Input.Keys.D,
        Binding.MOVE_UP to Input.Keys.W,
        Binding.MOVE_DOWN to Input.Keys.S,
        Binding.JUMP to Input.Keys.SPACE,
        Binding.USE to Input.Keys.E
    )

    fun bind(action: Binding, key: Int) {
        keyMap[action] = key
    }

    fun isPressed(action: Binding): Boolean {
        check(keyMap.containsKey(action)) { "There is no binding for $action action" }

        return Gdx.input.isKeyPressed(keyMap[action]!!)
    }

    fun isJustPressed(action: Binding): Boolean {
        check(keyMap.containsKey(action)) { "There is no binding for $action action" }

        return Gdx.input.isKeyJustPressed(keyMap[action]!!)
    }
}
