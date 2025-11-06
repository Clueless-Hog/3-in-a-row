package engine.map

import com.badlogic.gdx.math.Rectangle

data class InteractiveObject(
    val type: String,
    val rect: Rectangle,
    val properties: Map<String, Any?> = emptyMap()
)
