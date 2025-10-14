package org.cluelesshog.game.inventory.event

import org.cluelesshog.game.inventory.InventorySlot
import org.cluelesshog.game.inventory.Item

data class ItemAdded(val item: Item, val quantity: Int, val slot: InventorySlot) {
    init {
        require(quantity > 0) { "Количество предметов должно быть натуральным числом" }
    }
}

data class ItemRemoved(val item: Item, val quantity: Int, val slot: InventorySlot) {
    init {
        require(quantity > 0) { "Количество предметов должно быть натуральным числом" }
    }
}
