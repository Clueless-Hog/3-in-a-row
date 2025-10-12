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
