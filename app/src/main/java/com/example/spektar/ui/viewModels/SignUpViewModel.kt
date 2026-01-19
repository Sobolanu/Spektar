package com.example.spektar.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.domain.model.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.spektar.ui.viewModels.states.UserSignUpData
import com.example.spektar.ui.userLoginScreens.AuthEvent
import io.github.jan.supabase.auth.Auth
import kotlinx.coroutines.flow.update

class SignUpViewModel(
    private val accountService: AccountService
) : ViewModel() {

    private val _userSignUpData = MutableStateFlow(UserSignUpData())
    val userSignUpData: StateFlow<UserSignUpData> get() = _userSignUpData

    fun onEvent(event: AuthEvent) {
        when(event) {
            is AuthEvent.SetAvatar -> {
                _userSignUpData.update { it.copy(
                    avatar = event.avatar
                )}
            }
            is AuthEvent.SetEmail -> {
                _userSignUpData.update { it.copy(
                    email = event.newEmail
                )}
            }
            is AuthEvent.SetPassword -> {
                _userSignUpData.update { it.copy(
                    password = event.newPassword
                )}
            }
            is AuthEvent.SetUsername -> {
                _userSignUpData.update { it.copy(
                    username = event.newUsername
                )}
            }
            is AuthEvent.SignUp -> {
                viewModelScope.launch {
                    accountService.signUp(userSignUpData.value)
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
