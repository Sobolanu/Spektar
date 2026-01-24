package com.example.spektar.ui.settingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.ui.common.AppLocaleManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class LanguageChangePhase {
    Idle, FadeIn, Changing, FadeOut
}

data class LanguageState(
    val selectedLanguage: String = "en",
    val phase: LanguageChangePhase = LanguageChangePhase.Idle
)


class SettingsViewModel(
    private val appLocaleManager: AppLocaleManager
) : ViewModel() {
    private val _settingState = MutableStateFlow(LanguageState())
    val settingState: StateFlow<LanguageState> = _settingState

    init {
        loadInitialLanguage()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.SelectLanguage -> {
                viewModelScope.launch {
                    // 1: fade in
                    _settingState.value = _settingState.value.copy(phase = LanguageChangePhase.FadeIn)
                    delay(300) // let fade-in animation run

                    // 2: trigger language change
                    _settingState.value = _settingState.value.copy(phase = LanguageChangePhase.Changing)
                    changeLanguage(event.language)
                    // or appLocaleManager.changeLanguage(event.language)

                    // 3: reload state
                    _settingState.value = _settingState.value.copy(
                        selectedLanguage = event.language,
                        phase = LanguageChangePhase.FadeOut
                    )

                    delay(300) // let fade-out animation run
                    _settingState.value = _settingState.value.copy(phase = LanguageChangePhase.Idle)
                }
            }
        }
    }

    private fun loadInitialLanguage() {
        val currentLanguage = appLocaleManager.getLanguageCode()
        _settingState.value = _settingState.value.copy(selectedLanguage = currentLanguage)
    }

    fun changeLanguage(languageCode: String) {
        appLocaleManager.changeLanguage(languageCode)
        _settingState.value = _settingState.value.copy(selectedLanguage = languageCode)
    }
}

@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(
    private val appLocaleManager: AppLocaleManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(
                appLocaleManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}