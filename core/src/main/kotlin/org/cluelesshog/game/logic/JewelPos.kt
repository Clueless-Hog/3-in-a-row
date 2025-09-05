package org.cluelesshog.game.logic

data class JewelPos(val column: Int, val row: Int) {
    override fun toString() = "pos{$column:$row}"
}
