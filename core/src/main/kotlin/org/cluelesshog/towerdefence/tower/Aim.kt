package org.cluelesshog.towerdefence.tower

import org.cluelesshog.engine.geometry.Position

interface Aim {
    fun predict(from: Position, at: Target, projectileSpeed: Float): Position
}
