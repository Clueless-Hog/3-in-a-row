package org.cluelesshog.game.ai

import org.cluelesshog.game.logic.Board

object SwapAI {
    fun randomSwap(board: Board) {
        val move = board.getPossibleMoves().random()
        board.swap(move.first, move.second)
    }
}
