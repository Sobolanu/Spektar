package com.example.spektar.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.domain.model.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.spektar.ui.viewModels.states.SignUpRequest
import com.example.spektar.ui.userLoginScreens.AuthEvent
import kotlinx.coroutines.flow.update

class SignUpViewModel(
    private val accountService: AccountService
) : ViewModel() {

    private val _signUpRequest = MutableStateFlow(SignUpRequest())
    val signUpRequest: StateFlow<SignUpRequest> get() = _signUpRequest

    fun onEvent(event: AuthEvent) {
        when(event) {
            is AuthEvent.SetAvatar -> {
                _signUpRequest.update { it.copy(
                    avatar = event.avatar
                )}
            }
            is AuthEvent.SetEmail -> {
                _signUpRequest.update { it.copy(
                    email = event.newEmail
                )}
            }
            is AuthEvent.SetPassword -> {
                _signUpRequest.update { it.copy(
                    password = event.newPassword
                )}
            }
            is AuthEvent.SetUsername -> {
                _signUpRequest.update { it.copy(
                    username = event.newUsername
                )}
            }
            is AuthEvent.SignUp -> {
                // here i get error FOREIGN KEY constraint failed (code 787 SQLITE_CONSTRAINT_FOREIGNKEY[787])
                viewModelScope.launch {
                    accountService.signUp(signUpRequest.value)
                }
            }
            is AuthEvent.SignIn -> { } // empty
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
