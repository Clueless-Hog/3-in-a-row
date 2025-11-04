package org.cluelesshog.game

import engine.Config
import engine.SceneController
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import org.cluelesshog.game.match3.logic.RNG
import org.cluelesshog.game.mainMenu.MainMenuScreen

class Launcher : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        SceneController.display = this
        SceneController.set<MainMenuScreen>()
        if (Config.DEBUG_MODE) {
            print("Game seed: ${RNG.seedValue}")
        }
    }
}
