package org.cluelesshog.game

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync

class Launcher : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        SceneController.display = this
        SceneController.set<MainMenuScreen>()
    }
}
