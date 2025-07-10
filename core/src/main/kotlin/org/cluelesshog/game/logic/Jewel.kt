package org.cluelesshog.game.logic

object jewelId {
    private var lastId = 0

    fun getNext() = ++lastId
}

data class Jewel(var column: Int, var row: Int, var type: JewelType, var id: Int = jewelId.getNext()) {
    fun moveTo(column: Int, row: Int) {
        this.column = column
        this.row = row
    }

    fun swap(jewel: Jewel) {
        val temp = jewel.copy()
        jewel.moveTo(column, row)
        this.moveTo(temp.column, temp.row)
    }
}
