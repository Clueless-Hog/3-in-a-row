package org.cluelesshog.game.inventory.event

import org.cluelesshog.game.inventory.Inventory
import org.cluelesshog.game.inventory.Item
import org.cluelesshog.game.logic.Match

class StoreMinedOre {
    operator fun invoke(event: Match) {
        event.matches.forEach {
            val item = Item(it.type.name)
            Inventory.store(item)
        }
    }
}
