package org.cluelesshog.game.mainMenu

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import engine.Scene
import engine.SceneController
import ktx.actors.onClick
import org.cluelesshog.game.asset.SoundManager
import org.cluelesshog.game.asset.SoundType
import org.cluelesshog.game.match3.view.GameScreen

class MainMenuScreen : Scene() {
    init {
        val table = Table()
        table.setFillParent(true)
        wrapper.addActor(table)

        val start = TextButton("Start Game", theme)

        start.onClick {
            SoundManager.playSound(SoundType.CLICK)
            SceneController.set<GameScreen>()
        }

        table.add(start).width(200f).height(50f).padBottom(10f)
    }

    override fun load() = true
}
