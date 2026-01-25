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
        // 1. Check for App-specific override
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

        // 2. Fallback: Get the actual language the system is currently using
        val systemLocales = ConfigurationCompat.getLocales(context.resources.configuration)
        val systemLanguage = systemLocales.get(0)?.language ?: ""

        // 3. Match against your supported list
        return if (appLanguages.any { it.code == systemLanguage }) {
            systemLanguage
        } else {
            getDefaultLanguageCode()
        }
    }

    /*
    fun getLanguageCode(): String {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java)
                ?.applicationLocales
                ?.get(0)
        } else {
            AppCompatDelegate.getApplicationLocales().get(0)
        }
        return locale?.language ?: getDefaultLanguageCode()
    }

     */

    private fun getDefaultLanguageCode(): String {
        return appLanguages.first().code
    }
}

