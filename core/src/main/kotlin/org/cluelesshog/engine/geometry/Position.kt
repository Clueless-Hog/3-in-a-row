package org.cluelesshog.engine.geometry

data class Position(val x: Int, val y: Int) {
    fun down(units: Int): Position {
        return Position(x, y - units)
    }

    fun up(units: Int): Position {
        return Position(x, y + units)
    }

    fun left(units: Int): Position {
        return Position(x - units, y)
    }

    fun right(units: Int): Position {
        return Position(x + units, y)
    }
}
