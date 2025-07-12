package org.cluelesshog.towerdefence.tower

import org.cluelesshog.engine.geometry.Position
import kotlin.math.hypot
import kotlin.math.sqrt

/**
 * TODO ChatGPT solution. Adjust, sanitize
 */
private data class Vector(val x: Float, val y: Float) {
    constructor(x: Int, y: Int) : this(x.toFloat(), y.toFloat())

    operator fun plus(other: Vector) = Vector(x + other.x, y + other.y)
    operator fun minus(other: Vector) = Vector(x - other.x, y - other.y)
    operator fun times(scalar: Float) = Vector(x * scalar, y * scalar)
    fun length() = hypot(x, y)
    fun normalize(): Vector {
        val len = length()
        return if (len == 0f) Vector(0f, 0f) else Vector(x / len, y / len)
    }
}

class SmartAim : Aim {
    override fun predict(
        from: Position,
        at: Target,
        projectileSpeed: Float
    ): Position {
        val currentTargetPosition = at.getPosition()
        val currentTargetDestination = at.movingTowards()
        if (at.speed() == 0f) {
            return at.getPosition()
        }

        return calculateInterceptPoint(
            Vector(from.x, from.y),
            projectileSpeed,
            Vector(currentTargetPosition.x, currentTargetPosition.y),
            Vector(currentTargetDestination.x, currentTargetDestination.y),
            at.speed()
        ) ?: at.getPosition()
    }

    /**
     * TODO ChatGPT solution. Adjust, sanitize
     *
     * @param object1Pos        Starting position of object1 (chaser)
     * @param object1Speed      Speed of object1 (units per second)
     * @param object2Pos        Current position of object2 (moving target)
     * @param object2Dest       Destination point of object2
     * @param object2Speed      Speed of object2 (units per second)
     * @return Predicted point where object1 should move to intercept object2, or null if not possible
     */
    private fun calculateInterceptPoint(
        object1Pos: Vector,
        object1Speed: Float,
        object2Pos: Vector,
        object2Dest: Vector,
        object2Speed: Float
    ): Position? {
        val toDestination = object2Dest - object2Pos
        val targetDirection = toDestination.normalize()
        val targetVelocity = targetDirection * object2Speed

        val relativePosition = object2Pos - object1Pos
        val relativeVelocity = targetVelocity

        val rx = relativePosition.x
        val ry = relativePosition.y
        val vx = relativeVelocity.x
        val vy = relativeVelocity.y

        val a = vx * vx + vy * vy - object1Speed * object1Speed
        val b = 2 * (rx * vx + ry * vy)
        val c = rx * rx + ry * ry

        val discriminant = b * b - 4 * a * c
        if (discriminant < 0 || a == 0f) return null // No real solution or linear case

        val sqrtDiscriminant = sqrt(discriminant)
        val t1 = (-b + sqrtDiscriminant) / (2 * a)
        val t2 = (-b - sqrtDiscriminant) / (2 * a)

        val timeToIntercept = listOf(t1, t2).filter { it > 0 }.minOrNull() ?: return null

        val interceptedPosition = object2Pos + targetVelocity * timeToIntercept

        return Position(interceptedPosition.x.toInt(), interceptedPosition.y.toInt())
    }

}
