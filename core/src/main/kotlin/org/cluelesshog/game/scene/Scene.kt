package org.cluelesshog.game.scene

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScalingViewport
import ktx.app.KtxScreen
import org.cluelesshog.game.Config
import org.cluelesshog.game.asset.Theme
import org.cluelesshog.game.scene.settings.Settings

abstract class Scene (
    protected val theme : Skin = Theme.default(),
    protected val wrapper: Stage = Stage(
        FitViewport(
            Settings.resolution.width.toFloat(),
            Settings.resolution.height.toFloat()
        )
    ),
    protected val hud: Stage = Stage(
        ScalingViewport(
            Scaling.fit,
            Settings.resolution.width.toFloat(),
            Settings.resolution.height.toFloat()
        )
    )
) : KtxScreen {
    private var isVisible = false

    private var isLoaded = false

    /**
     *  Сцена должна возвращать true при успешном завершении загрузки и false при неудачном
     *  Таким образом мы предотвращаем всевозможные ошибки, которые возникают при переходах между сценами.
     *  Например, когда сцена загрузки игры не находит сохранение и пытается вернуться в главное меню
     */
    protected abstract fun load(): Boolean

    override fun show() {
        if (!isLoaded) {
            isLoaded = load()
        }

        if (!isLoaded) {
            return
        }

        isVisible = true
        wrapper.isDebugAll = Config.DEBUG_MODE
        hud .isDebugAll = Config.DEBUG_MODE
        Gdx.input.inputProcessor = InputMultiplexer(wrapper, hud)
    }

    override fun hide() {
        isVisible = false
        Gdx.input.inputProcessor = null
    }

    final override fun render(delta: Float) {
        if (!isVisible) {
            return
        }

        wrapper.act(delta)
        hud.act(delta)

        wrapper.draw()
        hud.draw()
    }

    final override fun resize(width: Int, height: Int) {
        if (!isVisible) {
            return
        }

        wrapper.viewport.update(width, height, true)
        hud.viewport.update(width, height, true)

        onResize(width, height)
    }

    protected open fun onResize(newWidth: Int, newHeight: Int) {

    }

    override fun dispose() {
        wrapper.dispose()
        hud.dispose()
    }

    protected fun getScreenWidth() = wrapper.viewport.screenWidth

    protected fun getScreenHeight() = wrapper.viewport.screenHeight
}
