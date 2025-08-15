package org.cluelesshog.game.logic

data class SwapResult(
    val matches: List<JewelPos>,
    val movedJewels: MutableMap<JewelPos, Int>,
    val newJewels: List<Jewel>,
    val scoreUp: Int
)
