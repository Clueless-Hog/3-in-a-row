package org.cluelesshog.game

import ktx.app.KtxGame
import ktx.app.KtxScreen

class Launcher : KtxGame<KtxScreen>() {
    override fun create() {
        addScreen(GameScreen())
        setScreen<GameScreen>()
    }
}
