package engine.map

import com.badlogic.gdx.math.Rectangle
import org.cluelesshog.game.settings.Settings

class CollisionSystem(private val map: GameMap) {
    fun isBlocked(rect: Rectangle): Boolean {
        if (rect.x < 0f || rect.y < 0f ||
            rect.x + rect.width > Settings.resolution.width ||
            rect.y + rect.height > Settings.resolution.height
        ) {
            return true
        }

        return map.collisionRects.any { it.overlaps(rect) }
    }
}
