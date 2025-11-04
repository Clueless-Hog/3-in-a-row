package org.cluelesshog.game.match3.logic

import engine.event.EventBus
import org.cluelesshog.game.match3.logic.event.JewelSwapped
import org.cluelesshog.game.match3.logic.event.Match
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

    fun getJewelOrNull(pos: JewelPos) = grid[pos]

    fun getScore() = scoreSystem.score

    fun swap(firstPos: JewelPos, secondPos: JewelPos): Boolean {
        val first = getJewel(firstPos)
        val second = getJewel(secondPos)

        if (!checkMatch(first, second)) {
            return false
        }

        EventBus.post(JewelSwapped(firstPos, secondPos))

        val temp = first.copy()
        first.pos = second.pos
        second.pos = temp.pos

        grid[secondPos] = first
        grid[firstPos] = second

        var matches = MatchDetect.detect(this)
        while (matches.isNotEmpty()) {
            destroyJewels(matches)

            val movedJewels = applyGravity()
            val newJewels = refillBoard()
            var refreshed = false

            while (!hasPossibleMoves()) {
                randomRefill()
                refreshed = true
            }

            EventBus.post(
                Match(
                    matches,
                    movedJewels,
                    newJewels,
                    getScore(),
                    refreshed
                )
            )
            matches = MatchDetect.detect(this)
        }

        return true
    }

    fun hasPossibleMoves(): Boolean {
        for (jewel in grid.values) {
            for (neighbor in getNeighbors(jewel)) {
                if (checkMatch(jewel, neighbor)) return true
            }
        }
        return false
    }

    fun getPossibleMoves(): List<Pair<JewelPos, JewelPos>> {
        val possibleMoves = mutableListOf<Pair<JewelPos, JewelPos>>()
        val visited = mutableSetOf<Pair<JewelPos, JewelPos>>()

        for (jewel in this) {
            for (neighbor in getNeighbors(jewel)) {
                // Чтобы не проверять (A,B) и потом (B,A)
                val pair = listOf(jewel.pos, neighbor.pos).sortedBy { it.hashCode() }
                val key = pair[0] to pair[1]
                if (key in visited) continue
                visited += key

                if (checkMatch(jewel, neighbor)) {
                    possibleMoves += (jewel.pos to neighbor.pos)
                }
            }
        }

        return possibleMoves
    }


    private fun getNeighbors(jewel: Jewel): List<Jewel> {
        val (col, row) = jewel.pos

        val directions = listOf(
            1 to 0,
            -1 to 0,
            0 to 1,
            0 to -1
        )

        return directions.mapNotNull { (dx, dy) ->
            grid[JewelPos(col + dx, row + dy)]
        }
    }

    private fun destroyJewels(jewels: List<Jewel>) {
        for (jewel in jewels) {
            scoreSystem.upScore(jewel.type)
            grid.remove(jewel.pos)
        }
    }

    private fun applyGravity(): MutableMap<JewelPos, Int> {
        val result = mutableMapOf<JewelPos, Int>()

        for (col in 0 until columnsCount) {
            val columnJewels = (0 until rowsCount)
                .mapNotNull { row -> grid[JewelPos(col, row)] }

            columnJewels.forEachIndexed { newRow, jewel ->
                val oldPos = jewel.pos
                val newPos = JewelPos(col, newRow)

                if (oldPos.row > newRow) {
                    result[oldPos] = oldPos.row - newRow
                }

                jewel.pos = newPos
                grid[newPos] = jewel
            }

            for (row in columnJewels.size until rowsCount) {
                grid.remove(JewelPos(col, row))
            }
        }

        return result
    }

    private fun refillBoard(): List<Jewel> {
        val result = mutableListOf<Jewel>()
        for (col in 0 until columnsCount) {
            for (row in 0 until rowsCount) {
                val pos = JewelPos(col, row)
                if (grid[pos] == null) {
                    val newJewel = Jewel(pos, JewelType.random())
                    grid[pos] = newJewel
                    result += newJewel.copy()
                }
            }
        }
        return result
    }

    private fun checkMatch(from: Jewel, to: Jewel): Boolean {
        if (!from.isNeighbor(to)) return false

        val copyGrid = grid.toMutableMap()
        val first = from.pos
        val second = to.pos

        copyGrid[first] = to.copy(pos = first)
        copyGrid[second] = from.copy(pos = second)

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

    private fun randomRefill(): MutableMap<JewelPos, Jewel> {
        val result = mutableMapOf<JewelPos, Jewel>()
        for (row in 0 until rowsCount) {
            for (column in 0 until columnsCount) {
                val possible = JewelType.entries.filter { canPlace(column, row, it) }
                val chosenType = possible.random(RNG.seed)
                val pos = JewelPos(column, row)
                val jewel = Jewel(pos, chosenType)
                grid[pos] = jewel
                result[pos] = jewel
            }
        }
        return result
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

