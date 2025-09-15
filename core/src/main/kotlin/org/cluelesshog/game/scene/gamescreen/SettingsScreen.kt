package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import ktx.actors.onChangeEvent
import ktx.actors.onClick
import ktx.scene2d.checkBox
import ktx.scene2d.scene2d
import ktx.scene2d.table
import org.cluelesshog.game.Scene
import org.cluelesshog.game.SceneController
import org.cluelesshog.game.asset.SoundManager

class SettingsScreen : Scene() {
    override fun load(): Boolean {
        val table = Table()
        table.setFillParent(true)

        val volume = Table().apply {
            val slider = Slider(0f, 1f, 0.01f, false, theme)
            val counter = Label("100", theme)

            slider.value = 1f
            slider.onChangeEvent {
                Settings.volume = value
                counter.setText((value * 100).toInt().toString())
            }

            add(Label("Volume", theme)).padRight(10f).width(150f)

            add(slider).width(200f).padRight(10f)
            add(counter).width(100f)
        }

        val resolution = Table().apply {
            val defaultIndex = Settings.resolution.ordinal
            val numberOfResolutions = Resolution.entries.size.toFloat()
            val slider = Slider(0f, numberOfResolutions - 1, 1f, false, theme)
            val counter = Label(Resolution.entries[defaultIndex].toString(), theme)

            slider.value = defaultIndex.toFloat()
            slider.onChangeEvent {
                val res = Resolution.entries[value.toInt()]
                Settings.resolution = res
                counter.setText(res.toString())
            }

            add(Label("Resolution", theme)).padRight(10f).width(150f)

            add(slider).width(200f).padRight(10f)
            add(counter).width(100f)
        }

        val fullScreenMode = scene2d.table {
            checkBox("FullScreen").onClick {
                Settings.fullscreen = !Settings.fullscreen
            }
        }

        val goBack = TextButton("Continue", theme).apply {
            width = 100f
            height = 100f
            onClick {
                SoundManager.playSound(SoundType.CLICK)
                SceneController.set<GameScreen>()
            }
        }

        table.add(volume).padBottom(10f)
        table.row()
        table.add(resolution).padBottom(10f)
        table.row()
        table.add(fullScreenMode).padBottom(10f)
        table.row()
        table.add(goBack).width(200f).height(50f).center()

        wrapper.addActor(table)

        return true
    }

    override fun dispose() {
        wrapper.dispose()
    }

}
