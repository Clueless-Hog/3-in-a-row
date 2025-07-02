package org.cluelesshog.engine.scene

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.async.KtxAsync
import kotlin.time.Duration

object SceneController {
    lateinit var display: KtxGame<KtxScreen>

    inline fun <reified T: KtxScreen> set(afterDelay: Duration = Duration.Companion.ZERO) {
        KtxAsync.launch {
            if (afterDelay != Duration.Companion.ZERO) {
                delay(afterDelay)
            }
            if (!display.containsScreen<T>()) {
                display.addScreen(T::class.java.getDeclaredConstructor().newInstance())
            }
            display.setScreen<T>()
        }
    }
}

