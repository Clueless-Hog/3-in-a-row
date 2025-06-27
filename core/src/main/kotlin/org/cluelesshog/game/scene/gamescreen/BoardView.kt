package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import org.cluelesshog.game.asset.TextureUtils
import org.cluelesshog.game.asset.TextureUtils.loadTextureForJewelType
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.logic.Jewel

class BoardView(private val grid: Board, private var jewelSize: Float) {
    private val group = Group()

    init {
        group.apply {
            setSize(grid.width * jewelSize, grid.height * jewelSize)
            clearChildren()

            for ((pos, jewel) in grid.getBoard()) {
                val (x, y) = pos

                addActor(getJewelImage(jewel, x, y))
            }
        }

        alignment()
    }

    fun getActor(): Group = group

    private fun alignment() {
        val padding = 50
        val availableWidth = Gdx.graphics.width - 2 * padding
        val availableHeight = Gdx.graphics.height - 2 * padding

        jewelSize = minOf(availableWidth / grid.width, availableHeight / grid.height).toFloat()

        group.apply {
            setSize(grid.width * jewelSize, grid.height * jewelSize)

            for (actor in children) {
                actor.apply {
                    val x = (x / width).toInt()
                    val y = (y / height).toInt()

                    setSize(jewelSize, jewelSize)
                    setPosition(x * jewelSize, y * jewelSize)
                }
            }

            val centerX = padding + (availableWidth - width) / 2f
            val centerY = padding + (availableHeight - height) / 2f

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
