package org.cluelesshog.game.grid

class GridModel(val width: Int = 8, val height: Int = 8) {
    private val grid: MutableMap<Pair<Int, Int>, Cell> = mutableMapOf()

    init {
        val textures = CellTexture.entries

        fillGridRandomly()
    }

    fun getGrid() : Map<Pair<Int, Int>, Cell> {
        return grid.toMap()
    }

    private fun canPlace(x: Int, y: Int, candidate: CellTexture): Boolean {
        val left1 = grid[Pair(x - 1, y)]?.texture
        val left2 = grid[Pair(x - 2, y)]?.texture
        val down1 = grid[Pair(x, y - 1)]?.texture
        val down2 = grid[Pair(x, y - 2)]?.texture
        return !(left1 == candidate && left2 == candidate) && !(down1 == candidate && down2 == candidate)
    }

    private fun fillGridRandomly() {
        val textures = CellTexture.entries

        for (x in 0 until width) {
            for (y in 0 until height) {
                val possible = textures.filter { canPlace(x, y, it) }
                val chosen = if (possible.isEmpty()) textures.random() else possible.random()
                grid[Pair(x, y)] = Cell(x, y, chosen)
            }
        }
    }
}

