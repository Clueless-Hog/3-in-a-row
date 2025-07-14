package org.cluelesshog.game.logic

object JewelId {
    private var lastId = 0

    fun getNext() = ++lastId
}

data class Jewel(var pos: JewelPos, var type: JewelType, var id: Int = JewelId.getNext())
