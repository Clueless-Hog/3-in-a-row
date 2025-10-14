package org.cluelesshog.game.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import kotlin.jvm.java
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip
import com.badlogic.gdx.scenes.scene2d.ui.TooltipManager
import com.badlogic.gdx.scenes.scene2d.ui.Window

object Theme {
    private const val FONT_SIZE = 24
    private const val FONT_BORDER_WIDTH = 2f
    private const val SLIDER_MIN_HEIGHT = 10f
    private const val KNOB_MIN_SIZE = 20f

    private var cachedSkin: Skin? = null

    fun default(): Skin {
        cachedSkin?.let { return it }

        val skin = Skin()

        val font = createFont()
        val whiteDrawable = createWhiteDrawable()
        skin.add("default", font)
        skin.add("white", whiteDrawable, Drawable::class.java)

        createLabels(skin, font)
        createButtons(skin, font)
        createTextFields(skin, font)
        createSliders(skin)
        createCheckBoxes(skin, font)
        createWindows(skin, font)
        createLists(skin, font)
        createScrollPanes(skin)
        createSelectBoxes(skin, font)
        createTooltip(skin, whiteDrawable)

        cachedSkin = skin
        return skin
    }

    // --------------------------
    //        FONT
    // --------------------------
    private fun createFont(): BitmapFont {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("Osaka Regular-Mono.otf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter().apply {
            size = FONT_SIZE
            color = Color.WHITE
            borderColor = Color.BLACK
            borderWidth = FONT_BORDER_WIDTH
        }
        val font = generator.generateFont(parameter)
        generator.dispose()
        return font
    }

    // --------------------------
    //     WHITE DRAWABLE BASE
    // --------------------------
    private fun createWhiteDrawable(): Drawable {
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888).apply {
            setColor(Color.WHITE)
            fill()
        }
        val tex = Texture(pixmap)
        pixmap.dispose()
        return TextureRegionDrawable(TextureRegion(tex))
    }

    // --------------------------
    //         LABEL
    // --------------------------
    private fun createLabels(skin: Skin, font: BitmapFont) {
        skin.add("default", Label.LabelStyle(font, Color.WHITE))
    }

    // --------------------------
    //        BUTTON
    // --------------------------
    private fun createButtons(skin: Skin, font: BitmapFont) {
        skin.add("default", TextButton.TextButtonStyle().apply {
            up = skin.newDrawable("white", Color.DARK_GRAY)
            down = skin.newDrawable("white", Color.GRAY)
            this.font = font
        })
    }

    // --------------------------
    //       TEXT FIELD
    // --------------------------
    private fun createTextFields(skin: Skin, font: BitmapFont) {
        skin.add("default", TextField.TextFieldStyle().apply {
            background = skin.newDrawable("white", Color.DARK_GRAY)
            cursor = skin.newDrawable("white", Color.WHITE)
            selection = skin.newDrawable("white", Color.BLUE)
            this.font = font
            fontColor = Color.WHITE
            messageFont = font
            messageFontColor = Color.LIGHT_GRAY
        })
    }

    // --------------------------
    //         SLIDER
    // --------------------------
    private fun createSliders(skin: Skin) {
        skin.add("default-horizontal", Slider.SliderStyle().apply {
            background = skin.newDrawable("white", Color.GRAY).apply { minHeight = SLIDER_MIN_HEIGHT }
            knob = skin.newDrawable("white", Color.WHITE).apply {
                minWidth = KNOB_MIN_SIZE
                minHeight = KNOB_MIN_SIZE
            }
        })
    }

    // --------------------------
    //        CHECK BOX
    // --------------------------
    private fun createCheckBoxes(skin: Skin, font: BitmapFont) {
        skin.add("default", CheckBox.CheckBoxStyle().apply {
            checkboxOff = skin.newDrawable("white", Color.DARK_GRAY).apply {
                minWidth = 20f
                minHeight = 20f
            }
            checkboxOn = skin.newDrawable("white", Color.GREEN).apply {
                minWidth = 20f
                minHeight = 20f
            }
            this.font = font
            fontColor = Color.WHITE
        })
    }

    // --------------------------
    //         WINDOW
    // --------------------------
    private fun createWindows(skin: Skin, font: BitmapFont) {
        skin.add("default", Window.WindowStyle().apply {
            titleFont = font
            titleFontColor = Color.YELLOW
            background = skin.newDrawable("white", Color.DARK_GRAY)
        })
    }

    // --------------------------
    //           LIST
    // --------------------------
    private fun createLists(skin: Skin, font: BitmapFont) {
        skin.add("default", ListStyle().apply {
            this.font = font
            fontColorSelected = Color.WHITE
            fontColorUnselected = Color.LIGHT_GRAY
            selection = skin.newDrawable("white", Color.BLUE)
            background = skin.newDrawable("white", Color.DARK_GRAY)
        })
    }

    // --------------------------
    //        SCROLL PANE
    // --------------------------
    private fun createScrollPanes(skin: Skin) {
        skin.add("default", ScrollPane.ScrollPaneStyle().apply {
            background = skin.newDrawable("white", Color.DARK_GRAY)
            hScroll = skin.newDrawable("white", Color.GRAY).apply { minHeight = 8f }
            hScrollKnob = skin.newDrawable("white", Color.LIGHT_GRAY).apply {
                minHeight = 8f
                minWidth = 20f
            }
            vScroll = skin.newDrawable("white", Color.GRAY).apply { minWidth = 8f }
            vScrollKnob = skin.newDrawable("white", Color.LIGHT_GRAY).apply {
                minWidth = 8f
                minHeight = 20f
            }
        })
    }

    // --------------------------
    //        SELECT BOX
    // --------------------------
    private fun createSelectBoxes(skin: Skin, font: BitmapFont) {
        skin.add("default", SelectBox.SelectBoxStyle().apply {
            this.font = font
            fontColor = Color.WHITE
            background = skin.newDrawable("white", Color.DARK_GRAY)
            scrollStyle = skin.get("default", ScrollPane.ScrollPaneStyle::class.java)
            listStyle = skin.get("default", ListStyle::class.java)
            backgroundOpen = skin.newDrawable("white", Color.GRAY)
            backgroundOver = skin.newDrawable("white", Color(0.3f, 0.3f, 0.3f, 1f))
        })
    }

    private fun createTooltip(skin: Skin, background: Drawable) {
        val tooltipGenericBehavior = TooltipManager.getInstance()
        tooltipGenericBehavior.instant() // Tooltip appears instantly on hover
        tooltipGenericBehavior.initialTime = 0f
        tooltipGenericBehavior.subsequentTime = 0f
        tooltipGenericBehavior.resetTime = 0f
        skin.add("default", tooltipGenericBehavior)
        skin.add("default", TextTooltip.TextTooltipStyle(skin.get(Label.LabelStyle::class.java), background))
    }
}
