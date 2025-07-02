package org.cluelesshog.engine.geometry

import kotlin.math.atan2

data class Angle(val degrees: Float) {
    val radians: Float = (degrees * Math.PI / 180f).toFloat()

    companion object {
        fun degrees(degrees: Float) = Angle(degrees)
        fun degrees(degrees: Double) = Angle(degrees.toFloat())
        fun radians(radians: Double) = degrees(Math.toDegrees(radians))
        fun radians(radians: Float) = degrees(Math.toDegrees(radians.toDouble()))
        fun between(a: Position, b: Position) = radians(atan2((b.y - a.y).toDouble(), (b.x - a.x).toDouble()))
    }
}
