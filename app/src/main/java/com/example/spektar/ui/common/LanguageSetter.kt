package com.example.spektar.ui.common

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.ConfigurationCompat
import androidx.core.os.LocaleListCompat

data class Language(
    val code: String,
    val displayLanguage: String
)

val appLanguages = listOf(
    Language("sr", "Serbian"),
    Language("en", "English"),
)

class AppLocaleManager(
    private val context: Context
) {
    fun changeLanguage(languageCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(languageCode)
        } else {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
        }
    }
    fun getLanguageCode(): String {
        // check for app-specific override
        val appLanguage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val localeManager = context.getSystemService(Context.LOCALE_SERVICE) as LocaleManager
            val locales: LocaleList = localeManager.applicationLocales
            if (!locales.isEmpty) locales.get(0).language else null
        } else {
            val localesCompat: LocaleListCompat = AppCompatDelegate.getApplicationLocales()
            if (!localesCompat.isEmpty) localesCompat.get(0)?.language else null
        }

        if (appLanguage != null) {
            return appLanguage
        }

        // or use system language
        val systemLocales = ConfigurationCompat.getLocales(context.resources.configuration)
        val systemLanguage = systemLocales.get(0)?.language ?: ""

        // check to see if you have it in language list
        return if (appLanguages.any { it.code == systemLanguage }) {
            systemLanguage
        } else {
            getDefaultLanguageCode()
        }
    }

    private fun getDefaultLanguageCode(): String {
        return appLanguages.first().code
    }
}

