package org.cluelesshog.game.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.scenes.scene2d.ui.List
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox

object Theme {
    private const val FONT_SIZE = 24
    private const val FONT_BORDER_WIDTH = 2f

    private const val SLIDER_MIN_HEIGHT = 10f
    private const val KNOB_MIN_SIZE = 20f

    private var cachedSkin: Skin? = null

    fun default(): Skin {
        cachedSkin?.let { return it }

        val skin = Skin()

        // --- Font ---
        val generator = FreeTypeFontGenerator(Gdx.files.internal("Osaka Regular-Mono.otf"))
        val parameter = FreeTypeFontGenerator.FreeTypeFontParameter().apply {
            size = FONT_SIZE
            color = Color.WHITE
            borderColor = Color.BLACK
            borderWidth = FONT_BORDER_WIDTH
        }
        val bitmapFont = generator.generateFont(parameter)
        skin.add("default", bitmapFont)

        // --- White base texture ---
        val pixmap = Pixmap(1, 1, Pixmap.Format.RGBA8888).apply {
            setColor(Color.WHITE)
            fill()
        }
        val whiteTex = Texture(pixmap)
        skin.add("white", TextureRegionDrawable(TextureRegion(whiteTex)), Drawable::class.java)

        // --- Label ---
        skin.add("default", Label.LabelStyle(bitmapFont, Color.WHITE))

        // --- Button ---
        skin.add("default", TextButton.TextButtonStyle().apply {
            up = skin.newDrawable("white", Color.DARK_GRAY)
            down = skin.newDrawable("white", Color.GRAY)
            font = bitmapFont
        })

        // --- TextField ---
        skin.add("default", TextField.TextFieldStyle().apply {
            background = skin.newDrawable("white", Color.DARK_GRAY)
            cursor = skin.newDrawable("white", Color.WHITE)
            selection = skin.newDrawable("white", Color.BLUE)
            font = bitmapFont
            fontColor = Color.WHITE
            messageFont = bitmapFont
            messageFontColor = Color.LIGHT_GRAY
        })

        // --- Slider ---
        skin.add("default-horizontal", Slider.SliderStyle().apply {
            background = skin.newDrawable("white", Color.GRAY).apply { minHeight = SLIDER_MIN_HEIGHT }
            knob = skin.newDrawable("white", Color.WHITE).apply {
                minWidth = KNOB_MIN_SIZE
                minHeight = KNOB_MIN_SIZE
            }
        })

        // --- CheckBox ---
        skin.add("default", CheckBox.CheckBoxStyle().apply {
            checkboxOff = skin.newDrawable("white", Color.DARK_GRAY).apply {
                minWidth = 20f
                minHeight = 20f
            }

            // Checkmark (green square with white check)
            checkboxOn = skin.newDrawable("white", Color.GREEN).apply {
                minWidth = 20f
                minHeight = 20f
            }

            font = bitmapFont
            fontColor = Color.WHITE
        })

        // --- Window ---
        skin.add("default", Window.WindowStyle().apply {
            titleFont = bitmapFont
            titleFontColor = Color.YELLOW
            background = skin.newDrawable("white", Color.DARK_GRAY)
        })

        // --- List ---
        skin.add("default", List.ListStyle().apply {
            font = bitmapFont
            fontColorSelected = Color.WHITE
            fontColorUnselected = Color.LIGHT_GRAY
            selection = skin.newDrawable("white", Color.BLUE)
            background = skin.newDrawable("white", Color.DARK_GRAY)
        })

        // --- ScrollPane ---
        skin.add("default", ScrollPane.ScrollPaneStyle().apply {
            background = skin.newDrawable("white", Color.DARK_GRAY)
            hScroll = skin.newDrawable("white", Color.GRAY).apply { minHeight = 8f }
            hScrollKnob = skin.newDrawable("white", Color.LIGHT_GRAY).apply { minHeight = 8f; minWidth = 20f }
            vScroll = skin.newDrawable("white", Color.GRAY).apply { minWidth = 8f }
            vScrollKnob = skin.newDrawable("white", Color.LIGHT_GRAY).apply { minWidth = 8f; minHeight = 20f }
        })

        // --- SelectBox ---
        skin.add("default", SelectBox.SelectBoxStyle().apply {
            font = bitmapFont
            fontColor = Color.WHITE
            background = skin.newDrawable("white", Color.DARK_GRAY)
            scrollStyle = skin.get("default", ScrollPane.ScrollPaneStyle::class.java)
            listStyle = skin.get("default", List.ListStyle::class.java)
            // Optional: different backgrounds for different states
            backgroundOpen = skin.newDrawable("white", Color.GRAY) // When dropdown is open
            backgroundOver = skin.newDrawable("white", Color(0.3f, 0.3f, 0.3f, 1f)) // On hover
        })

        cachedSkin = skin

        generator.dispose()
        pixmap.dispose()

        return skin
    }
}
