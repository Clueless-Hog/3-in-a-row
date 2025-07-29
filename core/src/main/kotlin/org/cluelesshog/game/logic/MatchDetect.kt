package org.cluelesshog.game.logic

object MatchDetect {
    fun detect(board: Board): List<JewelPos> {
        val result = mutableListOf<JewelPos>()
        board.forEach {
            result += getMatches(it.pos, board)
        }
        return result
    }

    private fun getMatches(pos: JewelPos, board: Board): List<JewelPos> {
        val type = board.getJewel(pos).type

        fun matchesInDirection(dx: Int, dy: Int): List<JewelPos> {
            val matches = generateSequence(1) { it + 1 }
                .map { JewelPos(pos.column + dx * it, pos.row + dy * it) }
                .takeWhile { board.getJewelOrNull(it)?.type == type }
                .toList()

            return if (matches.size >= 2) matches + pos else emptyList()
        }

        val horizontal = matchesInDirection(-1, 0) + matchesInDirection(1, 0)
        val vertical = matchesInDirection(0, -1) + matchesInDirection(0, 1)

        return (horizontal + vertical).distinct()
    }
}
