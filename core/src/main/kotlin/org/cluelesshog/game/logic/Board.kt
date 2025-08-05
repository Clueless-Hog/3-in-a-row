package org.cluelesshog.game.logic

import kotlin.enums.EnumEntries
import kotlin.math.sqrt

class Board : Iterable<Jewel> {
    private val grid: MutableMap<JewelPos, Jewel>
    val columnsCount: Int
    val rowsCount: Int
    val scoreSystem = ScoreSystem()

    constructor(columnsCount: Int, rowsCount: Int) {
        grid = mutableMapOf()

        this.columnsCount = columnsCount
        this.rowsCount = rowsCount

        do {
            randomRefill()
        } while (!hasPossibleMoves())
    }

    constructor(grid: MutableMap<JewelPos, Jewel>) {
        this.grid = grid
        this.rowsCount = sqrt(grid.size.toDouble()).toInt()
        this.columnsCount = sqrt(grid.size.toDouble()).toInt()

        isRectangle(grid)
    }

    override fun iterator(): Iterator<Jewel> {
        return grid.values.iterator()
    }

    fun getJewel(pos: JewelPos) = grid[pos]!!

    fun getJewel(x: Int, y: Int) = getJewel(JewelPos(x, y))

    fun getJewelOrNull(pos: JewelPos) = grid[pos]

    fun getScore() = scoreSystem.score

    fun swap(firstPos: JewelPos, secondPos: JewelPos): Boolean {
        val first = getJewel(firstPos)
        val second = getJewel(secondPos)

        if (!checkMatch(first, second)) {
            return false
        }

        val temp = first.copy()
        first.pos = second.pos
        second.pos = temp.pos

        grid[secondPos] = first
        grid[firstPos] = second

        var matches = MatchDetect.detect(this)
        while (matches.isNotEmpty()) {
            destroyJewels(matches)
            matches = MatchDetect.detect(this)
        }

        while (!hasPossibleMoves()) {
            randomRefill()
        }

        return true
    }

    private fun hasPossibleMoves(): Boolean {
        for (jewel in grid.values) {
            for (neighborPos in jewel.pos.neighbors()) {
                val neighbor = grid[neighborPos] ?: continue
                if (checkMatch(jewel, neighbor)) return true
            }
        }
        return false
    }

    private fun destroyJewels(positions: List<JewelPos>) {
        for (pos in positions) {
            scoreSystem.upScore(getJewelOrNull(pos)?.type)
            grid.remove(pos)
        }
        applyGravity()
        refillBoard()
    }

    private fun applyGravity() {
        for (col in 0 until columnsCount) {
            val columnJewels = mutableListOf<Jewel>()
            for (row in 0 until rowsCount) {
                val pos = JewelPos(col, row)
                grid[pos]?.let { columnJewels.add(it) }
            }

            for (row in 0 until rowsCount) {
                val pos = JewelPos(col, row)
                if (row < columnJewels.size) {
                    val jewel = columnJewels[row]
                    jewel.pos = pos
                    grid[pos] = jewel
                } else {
                    grid.remove(pos)
                }
            }
        }
    }

    private fun refillBoard() {
        for (col in 0 until columnsCount) {
            for (row in 0 until rowsCount) {
                val pos = JewelPos(col, row)
                if (grid[pos] == null) {
                    grid[pos] = Jewel(pos, JewelType.random())
                }
            }
        }
    }

    private fun checkMatch(from: Jewel, to: Jewel): Boolean {
        if (!from.isNeighbor(to)) return false

        val copyGrid = grid.toMutableMap()
        val first = from.pos
        val second = to.pos

        copyGrid[first] = to.copy(first)
        copyGrid[second] = from.copy(second)

        val result = hasMatchAt(first, copyGrid) || hasMatchAt(second, copyGrid)

        return result
    }

    private fun hasMatchAt(pos: JewelPos, grid: MutableMap<JewelPos, Jewel>): Boolean {
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

    private fun randomRefill() {
        for (row in 0 until rowsCount) {
            for (column in 0 until columnsCount) {
                val possible = JewelType.entries.filter { canPlace(column, row, it) }
                val chosenType = possible.random(RNG.seed)
                val pos = JewelPos(column, row)
                grid[pos] = Jewel(pos, chosenType)
            }
        }
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

    private fun isRectangle(grid: MutableMap<JewelPos, Jewel>) {
        val leftBottom = grid.keys.minBy { it.row + it.column }
        val topRight = grid.keys.maxBy { it.row + it.column }

        for (row in leftBottom.row until topRight.row) {
            for (column in leftBottom.column until topRight.column) {
                require(grid.containsKey(JewelPos(column, row)))
            }
        }
    }
}

