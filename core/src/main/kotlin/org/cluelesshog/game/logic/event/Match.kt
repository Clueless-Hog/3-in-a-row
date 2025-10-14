package org.cluelesshog.game.logic.event

import org.cluelesshog.game.logic.Jewel
import org.cluelesshog.game.logic.JewelPos

data class Match(
    val matches: List<Jewel>,
    val movedJewels: Map<JewelPos, Int>,
    val newJewels: List<Jewel>,
    val scoreUp: Int,
    val refreshed: Boolean
)
