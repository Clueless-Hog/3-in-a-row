package org.cluelesshog.game.logic

import kotlin.enums.enumEntries
import kotlin.random.Random

object RNG {
    var seed: Random = Random.Default
        private set

    fun setSeed(newSeed: Int) {
        seed = Random(newSeed)
    }
}

enum class JewelType {
    DIAMOND,
    EMERALD,
    RUBY,
    AMETHYST;

    companion object {
        fun random(random: Random = RNG.seed): JewelType {
            return entries.random(random)
        }

        fun hasValue(name: String): Boolean {
            return enumEntries<JewelType>().find { it.name == name } != null
        }
    }
}
