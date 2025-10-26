package org.cluelesshog.game.inventory.event

import org.cluelesshog.game.inventory.Inventory
import org.cluelesshog.game.inventory.ItemRef
import org.cluelesshog.game.match3.logic.JewelType
import org.cluelesshog.game.match3.logic.event.Match

class StoreMinedOre {
    companion object {
        private val JEWEL_TO_ORE_MATCHER = mapOf(
            JewelType.AMETHYST to ItemRef.IRON,
            JewelType.EMERALD to ItemRef.ORICHALCUM,
            JewelType.RUBY to ItemRef.COPPER,
            JewelType.DIAMOND to ItemRef.QUARTZ,
        )
    }

    operator fun invoke(event: Match) {
        event.matches.forEach {
            val item = JEWEL_TO_ORE_MATCHER[it.type]!!
            Inventory.store(item)
        }
    }
}
