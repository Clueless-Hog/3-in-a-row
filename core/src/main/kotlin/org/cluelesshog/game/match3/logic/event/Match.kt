package org.cluelesshog.game.match3.logic.event

import org.cluelesshog.game.match3.logic.Jewel
import org.cluelesshog.game.match3.logic.JewelPos

data class Match(
    val matches: List<Jewel>,
    val movedJewels: Map<JewelPos, Int>,
    val newJewels: List<Jewel>,
    val scoreUp: Int,
    val refreshed: Boolean
)
