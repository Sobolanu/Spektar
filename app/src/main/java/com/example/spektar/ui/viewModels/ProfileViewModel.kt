package com.example.spektar.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.data.model.User
import com.example.spektar.domain.model.AccountService
import com.example.spektar.ui.profileScreen.ProfileEvent
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/* we need user data, so:
    - current PFP, username, email, password, account creation date

    in terms of possible events:
    - change username/password, reset user interactions (embeddings), sign out, delete account
 */

class ProfileViewModel(
    private val accountService: AccountService
) : ViewModel() {

    val _state = MutableStateFlow(User())
    val state : StateFlow<User> get() = _state

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            accountService.sessionFlow.collect { session ->
                if (session != null) {
                    val fullUserData = accountService.retrieveUserDataWithId(session.user!!.id) // i hope this always will pass

                    _state.value = User(
                        id = fullUserData.id,
                        username = fullUserData.username,
                        email =  session.user!!.email!!,
                        avatar_url = fullUserData.avatar_url?.let { accountService.makeUseableStorageUrl(it) },
                        accountCreationDate = session.user!!.createdAt.toString()
                    )
                } else {
                    Exception("Session is null at function loadData of ProfileScreenViewModel.")
                }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when(event) {
            ProfileEvent.deleteAccount -> {
                viewModelScope.launch {
                    accountService.deleteAccount() // add confirmation?
                }
            }

            ProfileEvent.resetPassword -> {
                viewModelScope.launch {
                    /* accountService.changePassword(
                        userId = TODO(),
                        oldPassword = " ", // obtained from state
                        newPassword = TODO() // obtained from state
                    ) */
                }
            }

            is ProfileEvent.resetUserSuggestions -> {

            }

            ProfileEvent.signOut -> {
                viewModelScope.launch {
                    accountService.signOut()
                }
            }

            is ProfileEvent.updateAvatar -> {
                viewModelScope.launch {
                    accountService.updateAvatar(event.userId, event.newAvatar, event.username)
                }
            }

            is ProfileEvent.updateUsername -> {

            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(
    private val accountService: AccountService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(
                accountService,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}