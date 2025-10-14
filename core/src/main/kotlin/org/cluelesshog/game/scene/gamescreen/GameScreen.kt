package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.Gdx.graphics
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import ktx.actors.onClick
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.scene2d
import ktx.scene2d.table
import org.cluelesshog.game.scene.Scene
import engine.SceneController
import org.cluelesshog.game.ai.SwapBot
import engine.event.EventBus
import org.cluelesshog.game.asset.SoundManager
import org.cluelesshog.game.asset.SoundType
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.craft.view.CraftScene
import org.cluelesshog.game.inventory.event.StoreMinedOre
import org.cluelesshog.game.logic.event.Match
import org.cluelesshog.game.scene.settings.Resolution
import org.cluelesshog.game.scene.settings.Settings
import org.cluelesshog.game.scene.settings.SettingsScreen

class GameScreen : Scene() {
    private var model: Board
    private var view: BoardView
    private var score: ScoreView
    private var swapBot: SwapBot
    private var settings: Button

    private val rows = 8
    private val columns = 8

    fun getBoardSize() = minOf(getScreenWidth() * 0.8f, getScreenHeight() * 0.8f)

    init {
        Settings.addObserver(::resolutionObserver)

        model = Board(rows, columns)

        score = ScoreView()
        score.setAlignment(Align.center)

        view = BoardView(model, score, getBoardSize(), getBoardSize())

        swapBot = SwapBot(model) { !view.isLocked() }

        // TODO принадлежит другому модулю. Продумать правильную систему инициализации и приостановки
        EventBus.subscribe<Match> {
            val handler = StoreMinedOre()
            handler(it)
        }

        settings = TextButton("Settings", theme)
        settings.onClick {
            SoundManager.playSound(SoundType.CLICK)
            SceneController.set<SettingsScreen>()
        }

        val craftScreen = TextButton("Craft", theme)
        craftScreen.onClick {
            SceneController.set<CraftScene>()
        }

        Scene2DSkin.defaultSkin = theme
        val ui = scene2d.table {
            setFillParent(true)

            top().pad(30f)
            add(settings).width(200f).height(50f).left().expandX()
            add(swapBot).width(100f).height(100f).left()
            add(score).width(200f)
            row()
            add(craftScreen).width(200f).height(50f).left().expandX().padTop(1f)
            row()

        }
        val gameField = scene2d.table {
            setFillParent(true)

            add(view).expand().center()
        }

        wrapper.addActor(gameField)
        hud.addActor(ui)
    }

    override fun load() = true

    private fun resolutionObserver(settings: Settings) {
        val res = settings.resolution

        if (settings.fullscreen)
            setResolutionInFullscreen(res)
        else
            graphics.setWindowedMode(res.width, res.height)

        resize(res.width, res.height)
    }

    private fun setResolutionInFullscreen(res: Resolution) {
        if (!graphics.supportsDisplayModeChange()) return

        val currentMonitor = graphics.monitor
        val modes = graphics.getDisplayModes(currentMonitor)

        val mode = modes.first { it.width == res.width && it.height == res.height }

        graphics.setFullscreenMode(mode)
    }

}
