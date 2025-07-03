import android.content.Context
import android.preference.PreferenceManager
import androidx.compose.ui.graphics.Color

class PreferencesManager(context: Context) {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_LANGUAGE = "language"
    }

    fun saveDarkModePreference(isDarkMode: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE, isDarkMode).apply()
    }

    fun loadDarkModePreference(): Boolean {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false)
    }

    fun saveLanguagePreference(language: String) {
        sharedPreferences.edit().putString(KEY_LANGUAGE, language).apply()
    }

    fun loadLanguagePreference(): String {
        return sharedPreferences.getString(KEY_LANGUAGE, "en") ?: "en"
    }

}
