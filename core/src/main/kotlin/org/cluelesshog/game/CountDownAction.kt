package org.cluelesshog.game

import com.badlogic.gdx.scenes.scene2d.Action

class CountDownAction : Action() {
    private var count = 0

    override fun act(delta: Float): Boolean {
        --count
        return true
    }

    fun setCount(value: Int) {
        count = value
    }

    val isComplete: Boolean
        get() = count == 0
}
