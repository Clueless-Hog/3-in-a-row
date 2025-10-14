package engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import ktx.preferences.get
import ktx.preferences.set
import ktx.preferences.flush

// Место, где данные будут сохраняться между перезапусками игры и между сценами
object GameSave {
    // По сути будет ссылаться на файл в папке пользователя. Например на unix ~/.prefs/game1.save
    val data: Preferences = Gdx.app.getPreferences("game1.save")

    inline fun <reified T> getOrNull(name: String): T? {
        return data[name]
    }

    // warning: передача псевдотипов таких как UInt приведет к неожиданным результатам
    // TODO сделать контракт более ограниченным и надежным
    fun save(name: String, value: Any) {
        data[name] = value
    }

    fun delete(name: String) {
        data.remove(name)
    }

    fun persisting(actions: GameSave.() -> Unit) {
        data.flush {
            actions()
        }
    }
}
