package org.cluelesshog.game.logic

import kotlin.math.abs

object JewelId {
    private var lastId = 0

    fun getNext() = ++lastId
}

data class Jewel(var pos: JewelPos, var type: JewelType, val id: Int = JewelId.getNext()) {
    fun isNeighbor(with: Jewel): Boolean {
        return (abs(pos.row - with.pos.row) == 1 && abs(pos.column - with.pos.column) == 0)
            || (abs(pos.row - with.pos.row) == 0 && abs(pos.column - with.pos.column) == 1)
    }
}
