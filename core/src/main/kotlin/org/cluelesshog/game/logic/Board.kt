package org.cluelesshog.game.logic

import kotlin.math.abs
import kotlin.math.sqrt

class Board {
    private val grid: MutableMap<JewelPos, Jewel>
    val columnsCount: Int
    val rowsCount: Int

    constructor(columnsCount: Int, rowsCount: Int) {
        grid = mutableMapOf()

        val textures = JewelType.entries

        for (row in 0 until rowsCount) {
            for (column in 0 until columnsCount) {
                val possible = textures.filter { canPlace(column, row, it) }
                val chosenType = possible.random()
                val pos = JewelPos(column, row)
                grid[pos] = Jewel(pos, chosenType)
            }
        }

        this.columnsCount = columnsCount
        this.rowsCount = rowsCount
    }

    constructor(grid: MutableMap<JewelPos, Jewel>)  {
        this.grid = grid
        this.rowsCount = sqrt(grid.size.toDouble()).toInt()
        this.columnsCount = sqrt(grid.size.toDouble()).toInt()

        require(columnsCount * rowsCount == grid.size){
            "Доска должна быть прямоугольной"
        }
    }

    fun getBoard(): Map<JewelPos, Jewel> {
        return grid.toMap()
    }

    fun getJewel(fromJewelPos: JewelPos) = grid[fromJewelPos]!!

    fun swap(firstPos: JewelPos, secondPos: JewelPos): Boolean {
        val first = grid[firstPos]!!
        val second = grid[secondPos]!!

        if (isValidSwap(first, second)) {
            val temp = first.copy()
            first.pos = JewelPos(second.pos.column, second.pos.row)
            second.pos = JewelPos(temp.pos.column, temp.pos.row)

            grid[secondPos] = first
            grid[firstPos] = second

            return true
        }
        return false
    }

    private fun isValidSwap(first: Jewel, second: Jewel): Boolean {
        val isNeighbors = (abs(first.pos.row - second.pos.row) == 1 && abs(first.pos.column - second.pos.column) == 0)
            || (abs(first.pos.row - second.pos.row) == 0 && abs(first.pos.column - second.pos.column) == 1)

        return isNeighbors && (checkMatch(first, second) || checkMatch(second, first))
    }

    private fun checkMatch(from: Jewel, to: Jewel): Boolean {
        val pos1 = from.pos
        val pos2 = to.pos

        grid[pos1] = to.copy(pos = pos1)
        grid[pos2] = from.copy(pos = pos2)

        val result = hasMatchAt(pos1) || hasMatchAt(pos2)

        grid[pos1] = from
        grid[pos2] = to

        return result
    }

    private fun hasMatchAt(pos: JewelPos): Boolean {
        val type = grid[pos]?.type ?: return false

        fun countInDirection(dx: Int, dy: Int): Int {
            return generateSequence(1) { it + 1 }
                .map { JewelPos(pos.column + dx * it, pos.row + dy * it) }
                .take(2)
                .takeWhile { grid[it]?.type == type }
                .count()
        }

        val horizontal = 1 + countInDirection(-1, 0) + countInDirection(1, 0)
        val vertical = 1 + countInDirection(0, -1) + countInDirection(0, 1)

        return horizontal >= 3 || vertical >= 3
    }

    private fun canPlace(x: Int, y: Int, candidate: JewelType): Boolean {
        val left1 = grid[JewelPos(x - 1, y)]?.type
        val left2 = grid[JewelPos(x - 2, y)]?.type

        if (left1 == candidate && left2 == candidate) {
            return false
        }

        val down1 = grid[JewelPos(x, y - 1)]?.type
        val down2 = grid[JewelPos(x, y - 2)]?.type

        if (down1 == candidate && down2 == candidate) {
            return false
        }

        return true
    }

}

