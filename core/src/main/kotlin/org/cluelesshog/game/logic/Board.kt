package org.cluelesshog.game.logic

class Board(val rows: Int = 8, val columns: Int = 8) {
    private val grid: MutableMap<Pair<Int, Int>, Jewel> = mutableMapOf()

    init {
        val textures = JewelType.entries

        for (row in 0 until rows) {
            for (column in 0 until columns) {
                val possible = textures.filter { canPlace(row, column, it) }
                val chosen = possible.random()
                grid[Pair(row, column)] = Jewel(row, column, chosen)
            }
        }
    }

    fun getBoard() : Map<Pair<Int, Int>, Jewel> {
        return grid.toMap()
    }

    private fun canPlace(x: Int, y: Int, candidate: JewelType): Boolean {
        val left1 = grid[Pair(x - 1, y)]?.type
        val left2 = grid[Pair(x - 2, y)]?.type

        if (left1 == candidate && left2 == candidate) {
            return false
        }

        val down1 = grid[Pair(x, y - 1)]?.type
        val down2 = grid[Pair(x, y - 2)]?.type

        if (down1 == candidate && down2 == candidate) {
            return false
        }

        return true
    }

}

