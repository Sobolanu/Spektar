package com.example.spektar.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.ui.viewModels.states.SignInRequest
import com.example.spektar.domain.model.AccountService
import com.example.spektar.ui.userLoginScreens.AuthEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// introduce login with username?
class SignInViewModel(
    private val accountService: AccountService
) : ViewModel() {
     private val _signInRequest = MutableStateFlow(SignInRequest())
     val signInRequest: StateFlow<SignInRequest> get() = _signInRequest

    fun onEvent(event : AuthEvent) { // should this be a suspend fun?
        when(event) {
            is AuthEvent.SetEmail -> {
                _signInRequest.update { it.copy(
                    email = event.newEmail
                )}
            }
            is AuthEvent.SetPassword -> {
                _signInRequest.update { it.copy(
                    password = event.newPassword
                )}
            }
            is AuthEvent.SignIn -> {
                viewModelScope.launch {
                    accountService.signIn(signInRequest.value)
                }
            }

            is AuthEvent.SetUsername -> { } // probably nothing too though i will add log-in via username
            is AuthEvent.SetAvatar -> { } // nothing happens, you can't use avatars in the sign-in screen
            is AuthEvent.SignUp -> { } // can't log in inside of SignUp.
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SignInViewModelFactory(
    private val accountService: AccountService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(
                accountService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
