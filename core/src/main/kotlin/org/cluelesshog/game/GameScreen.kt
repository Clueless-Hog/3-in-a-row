package org.cluelesshog.game

import com.badlogic.gdx.scenes.scene2d.ui.Label

class GameScreen : Scene() {
    override fun load(): Boolean {
        wrapper.addActor(Label("Game", theme))
        return true
    }
}
