package com.example.spektar.ui.userAuthScreens.signUpScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.domain.model.services.AccountService
import com.example.spektar.ui.userAuthScreens.AuthEvent
import com.example.spektar.ui.userAuthScreens.states.SignUpState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val accountService: AccountService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> get() = _signUpState

    fun onEvent(event: AuthEvent) {
        when(event) {
            is AuthEvent.SetAvatar -> {
                _signUpState.update { it.copy(
                    avatar = event.avatar
                )}
            }
            is AuthEvent.SetEmail -> {
                _signUpState.update { it.copy(
                    email = event.newEmail
                )}
            }
            is AuthEvent.SetPassword -> {
                _signUpState.update { it.copy(
                    password = event.newPassword
                )}
            }
            is AuthEvent.SetUsername -> {
                _signUpState.update { it.copy(
                    username = event.newUsername
                )}
            }
            is AuthEvent.SignUp -> {
                viewModelScope.launch(ioDispatcher) {
                    accountService.signUp(signUpState.value).fold(
                        ifLeft = { failure ->
                            // handle failure here
                        },
                        ifRight = { success ->
                            // leave empty
                        }
                    )
                }
            }
            is AuthEvent.SignIn -> { } // keep empty, as you cannot go to signIn from this ViewModel.
        }
    }
}

@Suppress("UNCHECKED_CAST")
class SignUpViewModelFactory(
    private val accountService: AccountService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            return SignUpViewModel(
                accountService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
