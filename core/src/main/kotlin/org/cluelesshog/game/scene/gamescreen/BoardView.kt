package org.cluelesshog.game.scene.gamescreen

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import org.cluelesshog.game.asset.TextureUtils.loadTextureForJewelType
import org.cluelesshog.game.logic.Board
import org.cluelesshog.game.logic.Jewel

class BoardView(private val grid: Board, private var jewelSize: Float) {
    private val group = Group()

    init {
        group.setSize(grid.width * jewelSize, grid.height * jewelSize)

        group.clearChildren()

        for ((pos, jewel) in grid.getBoard()) {
            val (x, y) = pos

            val jewelImage = createJewelImage(jewel)
            jewelImage.setSize(jewelSize, jewelSize)
            jewelImage.setPosition(x * jewelSize, y * jewelSize)

            group.addActor(jewelImage)
        }
    }

    fun resize(newWidth: Int, newHeight: Int) {
        val padding = 50
        val availableWidth = newWidth - 2 * padding
        val availableHeight = newHeight - 2 * padding

        jewelSize = minOf(availableWidth / grid.width, availableHeight / grid.height).toFloat()

        group.setSize(grid.width * jewelSize, grid.height * jewelSize)

        for (actor in group.children) {
            val x = (actor.x / actor.width).toInt()
            val y = (actor.y / actor.height).toInt()

            actor.setSize(jewelSize, jewelSize)
            actor.setPosition(x * jewelSize, y * jewelSize)
        }

        val centerX = padding + (availableWidth - group.width) / 2f
        val centerY = padding + (availableHeight - group.height) / 2f
        group.setPosition(centerX, centerY)
    }

    fun getActor(): Group = group

    private fun createJewelImage(jewel: Jewel): Image {
        return Image(loadTextureForJewelType(jewel.type))
    }
}
