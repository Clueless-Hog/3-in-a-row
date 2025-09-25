package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import ktx.actors.onChange
import ktx.actors.onChangeEvent
import ktx.actors.onClick
import org.cluelesshog.game.Scene
import org.cluelesshog.game.SceneController
import org.cluelesshog.game.asset.SoundManager

class SettingsScreen : Scene() {
    init {
        val volume = Slider(0f, 1f, 0.01f, false, theme).apply {
            value = 1f
            onChangeEvent {
                Settings.volume = value
            }
        }

        val resolution = SelectBox<Resolution>(theme).apply {
            setItems(Resolution.SMALL, Resolution.MEDIUM, Resolution.LARGE)
            setAlignment(Align.center)

            list.alignment = Align.center

            selected = Settings.resolution

            onChange {
                Settings.resolution = selected
            }
            onClick {
                SoundManager.playSound(SoundType.CLICK)
            }
            list.onClick {
                SoundManager.playSound(SoundType.CLICK)
            }
        }

        val fullscreen = CheckBox("", theme).apply {
            isChecked = Settings.fullscreen

            onChange {
                Settings.fullscreen = isChecked
            }
        }

        val goBack = TextButton("Continue", theme).apply {
            onClick {
                SoundManager.playSound(SoundType.CLICK)
                SceneController.set<GameScreen>()
            }
        }

        val root = Table().apply {
            setFillParent(true)
            center()

            defaults().size(40f).pad(5f)

            columnDefaults(0).width(getScreenWidth() * 0.1f)
            columnDefaults(1).width(getScreenWidth() * 0.15f)

            add(Label("Volume", theme))
            add(volume).left().row()

            add(Label("Resolution", theme))
            add(resolution).height(40f).row()

            add(Label("Fullscreen", theme))
            add(fullscreen).width(25f).left().row()

            add(goBack).colspan(3).padTop(10f)
        }

        wrapper.addActor(root)
    }

    override fun load() = true
}
