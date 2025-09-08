package org.cluelesshog.game.logic

data class Match(
    val matches: List<JewelPos>,
    val movedJewels: Map<JewelPos, Int>,
    val newJewels: List<Jewel>,
    val scoreUp: Int,
    val refreshed: Boolean
)
