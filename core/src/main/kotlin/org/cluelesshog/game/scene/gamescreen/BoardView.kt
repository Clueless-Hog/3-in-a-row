package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import org.cluelesshog.game.asset.TextureUtils
import org.cluelesshog.game.asset.TextureUtils.loadTextureForJewelType
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.logic.Jewel

class BoardView(private val grid: Board, private val width: Int, private val height: Int) {
    private val group = Group()
    private var jewelSize = 32f

    init {
        group.apply {
            for ((pos, jewel) in grid.getBoard()) {
                val (x, y) = pos

                addActor(getJewelImage(jewel, x, y))
            }
        }

        alignToCenter()
    }

    fun getActor(): Group = group

    fun setPosition(x: Float, y: Float) {
        group.setPosition(x, y)
    }

    private fun alignToCenter() {
        val availableWidth = width
        val availableHeight = height

        jewelSize = minOf(availableWidth / grid.rows, availableHeight / grid.columns).toFloat()

        group.apply {
            setSize(grid.rows * jewelSize, grid.columns * jewelSize)

            for (actor in children) {
                actor.apply {
                    val x = (x / width).toInt()
                    val y = (y / height).toInt()

                    setSize(jewelSize, jewelSize)
                    setPosition(x * jewelSize, y * jewelSize)
                }
            }

            val centerX = (availableWidth - width) / 2f
            val centerY = (availableHeight - height) / 2f

            setPosition(centerX, centerY)
        }
    }

    private fun getJewelImage(jewel: Jewel, row: Int, column: Int): Image {
        return createJewelImage(jewel).apply {
            setSize(jewelSize, jewelSize)
            setPosition(row * jewelSize, column * jewelSize)

            addListener(object : ClickListener() {
                val original = drawable
                var isClicked = false

                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    isClicked = !isClicked

                    if (isClicked) {
                        highlightJewel(this@apply, row, column)
                    } else {
                        drawable = original
                        setSize(jewelSize, jewelSize)
                        setPosition(row * jewelSize, column * jewelSize)
                    }
                }
            })
        }
    }

    private fun highlightJewel(
        jewel: Image,
        row: Int,
        column: Int
    ) {
        jewel.apply {
            drawable = TextureUtils.getHighlightTexture(drawable)

            val newJewelSize = jewelSize * 1.2f
            val cellPosX = row * jewelSize
            val cellPosY = column * jewelSize
            val posX = cellPosX - ((newJewelSize - jewelSize) / 2)
            val posY = cellPosY - ((newJewelSize - jewelSize) / 2)

            setSize(newJewelSize, newJewelSize)
            setPosition(posX, posY)
        }
    }

    private fun createJewelImage(jewel: Jewel): Image {
        return Image(loadTextureForJewelType(jewel.type))
    }
}
