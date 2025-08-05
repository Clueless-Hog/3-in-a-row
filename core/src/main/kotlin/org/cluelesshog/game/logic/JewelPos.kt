package org.cluelesshog.game.logic

data class JewelPos(val column: Int, val row: Int) {
    fun neighbors() = listOf(
        JewelPos(column - 1, row),
        JewelPos(column + 1, row),
        JewelPos(column, row - 1),
        JewelPos(column, row + 1)
    )
}
