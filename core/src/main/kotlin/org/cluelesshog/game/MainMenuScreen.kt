package org.cluelesshog.game

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class MainMenuScreen : Scene() {
    init {
        val table = Table()
        table.setFillParent(true)
        wrapper.addActor(table)

        val button = TextButton("Start Game", theme)
        button.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                SceneController.set<GameScreen>()
            }
        })

        table.add(button).width(200f).height(50f)
    }

    override fun load(): Boolean {
        return true
    }

}
