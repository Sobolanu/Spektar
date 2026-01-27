package com.example.spektar.ui.userAuthScreens.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.domain.model.services.AccountService
import com.example.spektar.ui.userAuthScreens.AuthEvent
import com.example.spektar.ui.userAuthScreens.states.SignInState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// introduce login with username?
class SignInViewModel(
    private val accountService: AccountService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
     private val _signInState = MutableStateFlow(SignInState())
     val signInState: StateFlow<SignInState> get() = _signInState

    fun onEvent(event : AuthEvent) {
        when(event) {
            is AuthEvent.SetEmail -> {
                _signInState.update { it.copy(
                    email = event.newEmail
                )}
            }

            is AuthEvent.SetPassword -> {
                _signInState.update { it.copy(
                    password = event.newPassword
                )}
            }

            is AuthEvent.SignIn -> {
                viewModelScope.launch(ioDispatcher) {
                    accountService.signIn(signInState.value).fold(
                        ifLeft = { failure ->
                            // handle failure here
                        },
                        ifRight = { success ->
                            // leave empty
                        }
                    )
                }

            }

            else -> { } // nothing happens as most other events cannot happen in this screen
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
