package org.cluelesshog.game.logic

object MatchDetect {
    fun detect(board: Board): Set<JewelPos> {
        val result = mutableSetOf<JewelPos>()
        val grid = board.getGrid()
        board.forEach {
            result += getMatches(it.pos, grid)
        }
        return result
    }

    private fun getMatches(pos: JewelPos, grid: Map<JewelPos, Jewel>): List<JewelPos> {
        val type = grid[pos]?.type

        fun matchesInDirection(dx: Int, dy: Int): List<JewelPos> {
            val matches = generateSequence(1) { it + 1 }
                .map { JewelPos(pos.column + dx * it, pos.row + dy * it) }
                .takeWhile { grid[it]?.type == type }
                .toList()

            return if (matches.size >= 2) matches + pos else emptyList()
        }

        val horizontal = matchesInDirection(-1, 0) + matchesInDirection(1, 0)
        val vertical = matchesInDirection(0, -1) + matchesInDirection(0, 1)

        return horizontal + vertical
    }
}
