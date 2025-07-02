package org.cluelesshog.towerdefence

import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import org.cluelesshog.game.SceneController
import org.cluelesshog.towerdefence.scene.ingame.GameScene

class Launcher : KtxGame<KtxScreen>() {
    override fun create() {
        KtxAsync.initiate()

        SceneController.display = this
        SceneController.set<GameScene>()
    }
}
