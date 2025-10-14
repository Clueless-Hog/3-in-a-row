package org.cluelesshog.game.logic

internal object MatchDetect {
    fun detect(board: Board): List<Jewel> {
        val result = mutableListOf<Jewel>()
        board.forEach {
            result += getMatches(it.pos, board)
        }
        return result.distinct()
    }

    private fun getMatches(pos: JewelPos, board: Board): List<Jewel> {
        val type = board.getJewel(pos).type

        fun matchesInDirection(dx: Int, dy: Int): List<Jewel> {
            val matches = generateSequence(1) { it + 1 }
                .map { JewelPos(pos.column + dx * it, pos.row + dy * it) }
                .takeWhile { board.getJewelOrNull(it)?.type == type }
                .map { board.getJewel(it) }
                .toList()

            if (matches.size < 2) {
                return emptyList()
            }

            return matches + board.getJewel(pos)
        }

        val horizontal = matchesInDirection(-1, 0) + matchesInDirection(1, 0)
        val vertical = matchesInDirection(0, -1) + matchesInDirection(0, 1)

        return (horizontal + vertical).distinct()
    }
}
