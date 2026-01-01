package com.example.spektar.screens.userScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.models.accountServices.AccountService
import com.example.spektar.models.repositories.BookRepository
import com.example.spektar.models.repositories.CategoryRepository
import com.example.spektar.models.repositories.GameRepository
import com.example.spektar.models.repositories.MovieRepository
import com.example.spektar.models.repositories.ShowRepository
import com.example.spektar.screens.mediaCategories.MediaViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.sign

data class SignInState(
    val email: String = "",
    val password: String = ""
)
class SignInViewModel(
    private val accountService: AccountService
) : ViewModel() {

     val _signInState = MutableStateFlow(SignInState())
     val signInState: StateFlow<SignInState> get() = _signInState

     init {
         _signInState.value = SignInState(
             email = "",
             password = ""
         )
     }

    fun updateEmail(newEmail: String) {
       _signInState.value = SignInState(
           email = newEmail,
           password = _signInState.value.password
       )
    }

    fun updatePassword(newPassword: String) {
        _signInState.value = SignInState(
            email = _signInState.value.email,
            password = newPassword
        )
    }

    fun onSignInClick() {
        viewModelScope.launch {
            accountService.signIn(signInState.value.email, signInState.value.password)
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
