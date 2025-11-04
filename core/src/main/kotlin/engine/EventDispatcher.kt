package engine

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

private typealias Event = Any

// TODO concurrent event bus
object EventDispatcher {
    private val _events = MutableSharedFlow<Event>(replay = 0)
    val events: SharedFlow<Event> = _events.asSharedFlow()

    suspend fun dispatch(event: Event) {
        _events.emit(event)
    }

    suspend fun subscribeFor(action: suspend (Event) -> Unit) {
        events.collect { event ->
            action(event)
        }
    }
}
