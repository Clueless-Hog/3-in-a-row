package org.cluelesshog.game.logic

import org.cluelesshog.game.Config
import kotlin.enums.enumEntries
import kotlin.random.Random

object RNG {
    var seedValue = Config.GAME_SEED
        private set
    var seed: Random = Random(seedValue)
        private set

    fun setSeed(newSeed: Int) {
        seedValue = newSeed.toLong()
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
