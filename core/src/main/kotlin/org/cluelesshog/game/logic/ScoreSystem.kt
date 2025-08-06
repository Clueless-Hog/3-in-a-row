package org.cluelesshog.game.logic

class ScoreSystem(score: Int = 0) {
    var score: Int = score
        private set

    fun upScore(type: JewelType?) {
        score += when (type) {
            JewelType.DIAMOND -> 10
            JewelType.EMERALD -> 15
            JewelType.RUBY -> 20
            JewelType.AMETHYST -> 5
            null -> 0
        }
    }
}
