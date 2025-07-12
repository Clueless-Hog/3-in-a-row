package org.cluelesshog.towerdefence.tower

import org.cluelesshog.engine.geometry.Position

interface Target {
    fun getPosition(): Position
    fun movingTowards(): Position
    fun speed(): Float
}
