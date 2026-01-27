package com.example.spektar.ui.userAuthScreens.loginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.data.remote.authService.UserAuthFailure
import com.example.spektar.domain.model.services.AccountService
import com.example.spektar.ui.userAuthScreens.AuthEvent
import com.example.spektar.ui.userAuthScreens.states.SignInState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
                            val errorMessage = when(failure) {
                                is UserAuthFailure.UsernameAlreadyExists -> "Username already exists. Please write a new name."
                                is UserAuthFailure.UnexpectedFailure -> "Unexpected authentication failure. Please try again."
                                is UserAuthFailure.ValidationFailed -> "User validation failed. Please try again."
                                is UserAuthFailure.EmailNotConfirmed -> "Please confirm your email before proceeding."
                                is UserAuthFailure.RequestTimeout -> "Request timed out. Please try again."
                                is UserAuthFailure.UserNotFound -> "Account does not exist. Check if your email and password are valid."
                                is UserAuthFailure.InvalidCredentials -> "Invalid credentials. Check if your email and password are correct."
                                else -> { "Unknown authentication error." }
                            }

                            _signInState.update{ it.copy(
                                snackBarText = errorMessage
                            )}

                            delay(100)

                            _signInState.update{ it.copy(
                                snackBarText = null
                            )}
                        },

                        ifRight = { // then success, just clear snackBarText if needed.
                            _signInState.update{ it.copy(
                                snackBarText = null,
                                signInFinished = true
                            )}
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
