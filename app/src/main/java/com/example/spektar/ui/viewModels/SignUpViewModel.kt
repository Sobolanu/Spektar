package com.example.spektar.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.domain.usecase.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

data class SignUpState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    var avatar : File? = null
)
class SignUpViewModel(
    private val accountService: AccountService
) : ViewModel() {

    val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> get() = _signUpState

    init {
        _signUpState.value = SignUpState(
            username = "",
            email = "",
            password = "",
            avatar = null
        )
    }

    fun updateUsername(newUsername: String) {
        _signUpState.value = SignUpState(
            username = newUsername,
            password = _signUpState.value.password,
            email = _signUpState.value.email,
            avatar = _signUpState.value.avatar,
        )
    }
    fun updateEmail(newEmail: String) {
        _signUpState.value = SignUpState(
            email = newEmail,
            password = _signUpState.value.password,
            username = _signUpState.value.username,
            avatar = _signUpState.value.avatar,
        )
    }

    fun updatePassword(newPassword: String) {
        _signUpState.value = SignUpState(
            email = _signUpState.value.email,
            password = newPassword,
            avatar = _signUpState.value.avatar,
            username = _signUpState.value.username,
        )
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            accountService.signUp(
                username = signUpState.value.username,
                userEmail = signUpState.value.email,
                userPassword = signUpState.value.password,
                avatar = signUpState.value.avatar,
            )
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
