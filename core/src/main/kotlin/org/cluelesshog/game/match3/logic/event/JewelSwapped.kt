package org.cluelesshog.game.match3.logic.event

import org.cluelesshog.game.match3.logic.JewelPos

data class JewelSwapped(val from: JewelPos, val to: JewelPos)
