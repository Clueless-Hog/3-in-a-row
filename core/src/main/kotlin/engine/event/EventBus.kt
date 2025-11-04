package engine.event

object EventBus {
    data class Subscription(val type: Class<*>, val listener: (Any) -> Unit)

    val listeners = mutableMapOf<Class<out Any>, MutableList<(Any) -> Unit>>()

    inline fun <reified T : Any> subscribe(noinline listener: (T) -> Unit): Subscription {
        val wrapper: (Any) -> Unit = { event -> listener(event as T) }
        listeners.getOrPut(T::class.java) { mutableListOf() }.add(wrapper)
        return Subscription(T::class.java, wrapper)
    }

    fun unsubscribe(subscription: Subscription) {
        listeners[subscription.type]?.remove(subscription.listener)
    }

    fun post(event: Any) {
        listeners[event::class.java]?.forEach { it(event) }
    }
}

fun signal(event: Any) {
    EventBus.post(event)
}

fun stopListening(process: EventBus.Subscription) {
    EventBus.unsubscribe(process)
}

inline fun <reified T : Any> listen(noinline listener: (T) -> Unit): EventBus.Subscription {
    return EventBus.subscribe<T>(listener)
}
