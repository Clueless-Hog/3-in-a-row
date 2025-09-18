package org.cluelesshog.game

import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import ktx.actors.onClick
import org.cluelesshog.game.asset.SoundManager
import org.cluelesshog.game.scene.gamescreen.GameScreen
import org.cluelesshog.game.scene.gamescreen.SoundType

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
