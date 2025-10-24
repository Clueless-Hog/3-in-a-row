package org.cluelesshog.game.ai

import org.cluelesshog.game.match3.logic.Board
import org.cluelesshog.game.match3.logic.RNG

object SwapAI {
    fun randomSwap(board: Board) {
        val move = board.getPossibleMoves().random(RNG.seed)
        board.swap(move.first, move.second)
    }
}
