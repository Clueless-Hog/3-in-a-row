package org.cluelesshog.game.inventory

data object ItemRef {
    val GOLD = Item("gold")
    val IRON = Item("iron")
    val ORICHALCUM = Item("orichalcum")
    val COPPER = Item("copper")
    val QUARTZ = Item("quartz")

    val IRON_SWORD = Item("iron_sword")
    val FLAMBERGE_SWORD = Item("flamberge_sword")
    val HELMET = Item("helmet")

    // TODO подключить рефлекшны или кодогенерацию
    val all = mapOf(
        GOLD.name to GOLD,
        IRON.name to IRON,
        ORICHALCUM.name to ORICHALCUM,
        COPPER.name to COPPER,
        QUARTZ.name to QUARTZ,
        IRON_SWORD.name to IRON_SWORD,
        FLAMBERGE_SWORD.name to FLAMBERGE_SWORD,
        HELMET.name to HELMET,
    )
}
