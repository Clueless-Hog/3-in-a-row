package org.cluelesshog.game.logic

import kotlin.math.abs

class Board(val rows: Int, val columns: Int) {
    private val grid: MutableMap<Pair<Int, Int>, Jewel> = mutableMapOf()

    init {
        val textures = JewelType.entries

        for (row in 0 until rows) {
            for (column in 0 until columns) {
                val possible = textures.filter { canPlace(column, row, it) }
                val chosen = possible.random()
                grid[column to row] = Jewel(column, row, chosen)
            }
        }
    }

    fun getBoard() : Map<Pair<Int, Int>, Jewel> {
        return grid
    }

    fun getJewel(fromPosition: Pair<Int, Int>) = grid[fromPosition]!!

    fun swap(firstPos: Pair<Int, Int>, secondPos: Pair<Int, Int>) : Boolean {
        val first = grid[firstPos]!!
        val second = grid[secondPos]!!

        if (isValidSwap(first, second)) {
            first.swap(second)
            grid[secondPos] = first
            grid[firstPos] = second

            return true
        }
        return false
    }

    fun isValidSwap(first: Jewel, second: Jewel): Boolean {
        return (abs(first.row - second.row) == 1 && abs(first.column - second.column) == 0)
            || (abs(first.row - second.row) == 0 && abs(first.column - second.column) == 1)
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

