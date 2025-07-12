package org.cluelesshog.towerdefence.tower

import org.cluelesshog.engine.geometry.Position

class CommonAim: Aim {
    override fun predict(
        from: Position,
        at: Target,
        projectileSpeed: Float
    ): Position {
        return at.getPosition()
    }
}
