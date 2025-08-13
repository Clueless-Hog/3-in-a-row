package org.cluelesshog.game.logic

data class SwapResult(val matches: List<JewelPos>, val currentBoard: Map<JewelPos, Jewel>)
