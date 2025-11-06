package engine.map

import com.badlogic.gdx.math.Rectangle

data class Portal(
    val rect: Rectangle,
    val targetMap: String,
    val targetX: Float,
    val targetY: Float
)

