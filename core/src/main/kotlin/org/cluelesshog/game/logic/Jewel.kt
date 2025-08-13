package org.cluelesshog.game.logic

import kotlin.math.abs

data class Jewel(var pos: JewelPos, var type: JewelType, var crushed: Boolean = false) {
    fun isNeighbor(with: Jewel): Boolean {
        return (abs(pos.row - with.pos.row) == 1 && abs(pos.column - with.pos.column) == 0)
            || (abs(pos.row - with.pos.row) == 0 && abs(pos.column - with.pos.column) == 1)
    }
}
