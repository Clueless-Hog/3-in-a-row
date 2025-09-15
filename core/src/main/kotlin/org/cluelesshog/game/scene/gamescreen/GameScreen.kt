package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import ktx.actors.onClick
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.scene2d
import ktx.scene2d.table
import org.cluelesshog.game.Scene
import org.cluelesshog.game.SceneController
import org.cluelesshog.game.asset.SoundManager
import org.cluelesshog.game.logic.Board

class GameScreen : Scene() {
    private lateinit var model: Board
    private lateinit var view: BoardView
    private lateinit var score: ScoreView
    private lateinit var settings: Button

    private val rows = 8
    private val columns = 8

    private val boardSize: Float
        get() = minOf(getScreenWidth() * 0.8f, getScreenHeight() * 0.8f)

    override fun dispose() {
        wrapper.dispose()
    }

    override fun load(): Boolean {
        Settings.addObserver(::resolutionObserver)
        Settings.addObserver(::fullscreenObserver)

        model = Board(rows, columns)
        score = ScoreView()
        view = BoardView(model, score, boardSize, boardSize)

        settings = TextButton("Settings", theme)
        settings.onClick {
            SoundManager.playSound(SoundType.CLICK)
            SceneController.set<SettingsScreen>()
        }

        Scene2DSkin.defaultSkin = theme
        val root = scene2d.table {
            setFillParent(true)

            top().pad(30f)
            add(settings).width(200f).height(50f).left().expandX()
            add(score).right().expandX()
            row()

            add(view).colspan(2).expand().center()
        }

        wrapper.addActor(root)
        return true
    }

    private fun fullscreenObserver(settings: Settings) {
        if (settings.fullscreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.displayMode)
            updateViewport()
            view.resize(boardSize, boardSize)
        } else {
            setResolution(settings.resolution)
        }
    }

    private fun resolutionObserver(settings: Settings) {
        setResolution(settings.resolution)
        updateViewport()
        view.resize(boardSize, boardSize)
    }

    private fun setResolution(res: Resolution) {
        Gdx.graphics.setWindowedMode(res.width, res.height)
    }

    private fun updateViewport() {
        wrapper.viewport.update(Gdx.graphics.width, Gdx.graphics.height, true)
    }
}
