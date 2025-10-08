package engine.event

object EventBus {
    val listeners = mutableMapOf<Class<out Any>, MutableList<(Any) -> Unit>>()

    inline fun <reified T : Any> subscribe(noinline listener: (T) -> Unit) {
        val list = listeners.getOrPut(T::class.java) { mutableListOf() }
        list.add { event -> listener(event as T) }
    }

    inline fun <reified T : Any> unsubscribe(noinline listener: (T) -> Unit) {
        listeners[T::class.java]?.removeIf { it == listener }
    }

    fun post(event: Any) {
        listeners[event::class.java]?.forEach { it(event) }
    }
}
