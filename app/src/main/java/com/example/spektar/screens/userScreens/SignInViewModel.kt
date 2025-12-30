package com.example.spektar.screens.userScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spektar.models.accountServices.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


/*
big dawg future me your code doesn't work here
because you need to define a good init {} block for your ViewModel
check your logcat just in case cause i might be dumb and may have misinterpreted the actual issue or smthn
 */
class SignInViewModel(
    private val accountService: AccountService
) : ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun updateEmail(newEmail: String) {
        email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        password.value = newPassword
    }

    fun onSignInClick() {
        viewModelScope.launch {
            accountService.signIn(email.value, password.value)
        }
    }
}