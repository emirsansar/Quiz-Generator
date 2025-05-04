package com.emirsansar.quizgenerator

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.*
import androidx.core.content.edit

object AppSettings {
    private const val PREF_NAME = "quiz_maker_preferences"
    private const val KEY_LANGUAGE = "selected_language"
    private const val KEY_THEME_MODE = "is_dark_theme"

    var selectedLanguage by mutableStateOf("English")
    var isDarkTheme by mutableStateOf(false)

    var isDeletedQuizzes by mutableStateOf(false)
    var isDeletedQuestions by mutableStateOf(false)

    var isSavedCreatedQuiz by mutableStateOf(false)

    // Call this once on app launch to initialize both language and theme.
    fun initialize(context: Context) {
        val prefs = getPrefs(context)

        // Initialize Language
        val storedLanguage = prefs.getString(KEY_LANGUAGE, null)
        if (storedLanguage == null) {
            val deviceLangCode = Locale.getDefault().language
            selectedLanguage = if (deviceLangCode == "tr") "Türkçe" else "English"
            prefs.edit { putString(KEY_LANGUAGE, selectedLanguage) }
        } else {
            selectedLanguage = storedLanguage
            updateAppLanguage(context, getLangCodeFromLabel(storedLanguage))
        }

        // Initialize Theme
        if (!prefs.contains(KEY_THEME_MODE)) {
            val isSystemDark = (context.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            isDarkTheme = isSystemDark
            prefs.edit().putBoolean(KEY_THEME_MODE, isDarkTheme).apply()
        } else {
            isDarkTheme = prefs.getBoolean(KEY_THEME_MODE, false)
        }
    }


    // Change app language and persist.
    fun changeLanguage(context: Context, newLanguage: String) {
        selectedLanguage = newLanguage
        updateAppLanguage(context, getLangCodeFromLabel(newLanguage))
        getPrefs(context).edit { putString(KEY_LANGUAGE, newLanguage) }
    }

    // Change app theme and persist.
    fun changeTheme(context: Context, darkThemeEnabled: Boolean) {
        isDarkTheme = darkThemeEnabled
        getPrefs(context).edit { putBoolean(KEY_THEME_MODE, isDarkTheme) }
    }

    // Applies the locale update.
    fun updateAppLanguage(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setLocale(locale)
            } else {
                this.locale = locale
            }
        }

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }


    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun getLangCodeFromLabel(label: String): String {
        return when (label) {
            "Türkçe" -> "tr"
            "English" -> "en"
            else -> "en"
        }
    }

}
