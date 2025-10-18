package engine

import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import ktx.async.KtxAsync

class Pipe {
    var isLocked = false
        private set
    private var isRunning = false
    private val actions = ArrayDeque<() -> Unit>()

    fun blocking(action: () -> Unit) {
        actions.add(action)
    }

    fun unlock() {
        isLocked = false
    }

    fun run() {
        if (isRunning) {
            return
        }
        isRunning = true

        KtxAsync.launch {
            while (true) {
                if (isLocked) {
                    yield()
                    continue
                }

                val action = actions.removeFirstOrNull()
                if (action != null) {
                    isLocked = true

                    action()
                } else {
                    yield()
                }
            }

        }
    }
}
