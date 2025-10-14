package org.cluelesshog.game.inventory

class InventorySlot(val cell: Int, var item: Item?, quantity: Int) {
    var quantity: Int = quantity
        set(value) {
            field = value
            if (field == 0) {
                item = null
            }
        }

    init {
        require(cell > 0) { "Слоты должны быть пронумерованы натуральными числами" }
    }

    fun isEmpty() = item == null

    fun label(): String {
        if (isEmpty()) {
            return "Empty"
        }

        return item!!.let { "${it.name} (${quantity})" }
    }

    companion object {
        fun newEmpty(cell: Int) = InventorySlot(cell, null, 0)
    }
}
