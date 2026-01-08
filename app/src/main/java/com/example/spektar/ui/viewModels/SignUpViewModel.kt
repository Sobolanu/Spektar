package com.example.spektar.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.domain.usecase.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SignUpState(
    val email: String = "",
    val password: String = ""
)
class SignUpViewModel(
    private val accountService: AccountService
) : ViewModel() {

    val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> get() = _signUpState

    init {
        _signUpState.value = SignUpState(
            email = "",
            password = ""
        )
    }

    fun updateEmail(newEmail: String) {
        _signUpState.value = SignUpState(
            email = newEmail,
            password = _signUpState.value.password
        )
    }

    fun updatePassword(newPassword: String) {
        _signUpState.value = SignUpState(
            email = _signUpState.value.email,
            password = newPassword
        )
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            accountService.signUp(signUpState.value.email, signUpState.value.password)
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
