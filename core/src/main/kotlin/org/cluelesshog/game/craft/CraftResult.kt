package org.cluelesshog.game.craft

import org.cluelesshog.game.inventory.Item

data class CraftResult(val item: Item, val quantity: Int) {
    init {
        require(quantity > 0)
    }
}
