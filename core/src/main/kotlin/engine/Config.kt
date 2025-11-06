package engine

import io.github.cdimascio.dotenv.dotenv

object Config {
    private val env = dotenv {
        filename = ".env.local"
        ignoreIfMissing = true
    }

    val DEBUG_MODE: Boolean = env.get("DEBUG_MODE")?.toBoolean() ?: true

    val GAME_SEED = env.get("GAME_SEED")?.toLong() ?: System.currentTimeMillis()
}
