package org.cluelesshog.game.scene.gamescreen

object Settings {
    var volume: Float = 1f
        set(value) {
            field = value.coerceIn(0f, 1f)
            notifyObservers()
        }

    var resolution: Resolution = Resolution.MEDIUM
        set(value) {
            field = value
            notifyObservers()
        }

    var fullscreen: Boolean = false
        set(value) {
            field = value
            notifyObservers()
        }

    private val observers = mutableListOf<(Settings) -> Unit>()

    fun addObserver(observer: (Settings) -> Unit): (Settings) -> Unit {
        observers += observer
        return observer
    }

    fun removeObserver(observer: (Settings) -> Unit) {
        observers -= observer
    }

    private fun notifyObservers() {
        observers.forEach { it(this) }
    }
}

enum class Resolution(val width: Int, val height: Int) {
    SMALL(1280, 720),     // HD
    MEDIUM(1920, 1080),   // Full HD
    LARGE(2560, 1440);    // QHD

    override fun toString() = "${width}x${height}"
}
