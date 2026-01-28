package com.example.spektar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.domain.model.services.AccountService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(
    accountService: AccountService
) : ViewModel() {
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _userAuthenticated = MutableStateFlow(false)
    val userAuthenticated = _userAuthenticated.asStateFlow()

    private val _signUpObserver = MutableStateFlow<Boolean?>(null)
    val signUpObserver = _signUpObserver.asStateFlow()

    init {
        // collect sessionFlow to update authentication state
        viewModelScope.launch {
            accountService.sessionFlow.collect { session ->
                _userAuthenticated.value = (session != null)
            }
        }

        // collect signUpObserver so we react to changes
        viewModelScope.launch {
            accountService.signUpObserver.collect { signUp ->
                _signUpObserver.value = signUp
            }
        }

        // splash delay
        viewModelScope.launch {
            delay(3000)
            _isReady.value = true
        }
    }
    /*
    init {
        viewModelScope.launch {
            accountService.sessionFlow.collect { session ->
                _userAuthenticated.value = (session != null)
                _signUpObserver.value = accountService.signUpObserver.value

                // if signUpObserver is false then user doesn't need to sign up
                // else if it is true then user must sign up
            }
        }

        // delay for the splash screen

        viewModelScope.launch {
            delay(3000)
            _isReady.value = true
        }
    } */
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val accountService: AccountService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                accountService,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}