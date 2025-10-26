package org.cluelesshog.game.craft

import org.cluelesshog.game.inventory.Item
import org.cluelesshog.game.inventory.ItemRef

data class Recipe(val requirements: Set<ItemRequirement>, val result: CraftResult) {
    fun name() = result.item.name
}

object RecipeRef {
    val ironSword = Recipe(
        setOf(
            ItemRequirement(ItemRef.IRON, 10),
            ItemRequirement(ItemRef.GOLD, 200),
        ),
        CraftResult(ItemRef.IRON_SWORD, 1)
    )

    val flambergeSword = Recipe(
        setOf(
            ItemRequirement(ItemRef.IRON, 8),
            ItemRequirement(ItemRef.COPPER, 5),
            ItemRequirement(ItemRef.GOLD, 700),
        ),
        CraftResult(ItemRef.FLAMBERGE_SWORD, 1)
    )

    val helmet = Recipe(
        setOf(
            ItemRequirement(ItemRef.IRON, 2),
            ItemRequirement(ItemRef.ORICHALCUM, 3),
            ItemRequirement(ItemRef.GOLD, 300),
        ),
        CraftResult(ItemRef.HELMET, 1)
    )

    val all = listOf(
        ironSword,
        flambergeSword,
        helmet,
    )
}
