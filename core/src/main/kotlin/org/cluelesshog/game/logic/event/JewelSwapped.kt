package org.cluelesshog.game.logic.event

import org.cluelesshog.game.logic.JewelPos

data class JewelSwapped(val from: JewelPos, val to: JewelPos)
