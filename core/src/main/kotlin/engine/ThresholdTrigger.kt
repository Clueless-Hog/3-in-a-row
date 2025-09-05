package engine

data class ThresholdTrigger(private val threshold: Int, private val callback: () -> Unit) {
    private var attempts = 0
    fun attempt() {
        if (++attempts == threshold) {
            callback()
        }
    }
}
