package org.cluelesshog.game.match3.logic

data class JewelPos(val column: Int, val row: Int) {
    override fun toString() = "pos{$column:$row}"
}
