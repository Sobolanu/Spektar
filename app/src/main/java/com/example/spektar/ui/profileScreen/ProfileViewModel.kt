package com.example.spektar.ui.profileScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.data.model.User
import com.example.spektar.domain.model.services.AccountService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/* we need user data, so:
    - current PFP, username, email, password, account creation date

    in terms of possible events:
    - change username/password, reset user interactions (embeddings), sign out, delete account
 */

class ProfileViewModel(
    private val accountService: AccountService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val _state = MutableStateFlow(User())
    val state : StateFlow<User> get() = _state

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch(ioDispatcher) {
            accountService.sessionFlow.collect { session ->
                if (session != null) {
                    val fullUserData = accountService.retrieveUserDataWithId(session.user!!.id) // i hope this always will pass

                    _state.value = User(
                        id = fullUserData.id,
                        username = fullUserData.username,
                        email =  session.user!!.email!!,
                        avatar_url = fullUserData.avatar_url?.let { accountService.storageUrl(it) },
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
                viewModelScope.launch(ioDispatcher) {
                    accountService.deleteAccount() // add confirmation?
                }
            }

            ProfileEvent.resetPassword -> {
                viewModelScope.launch(ioDispatcher){
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
                viewModelScope.launch(ioDispatcher) {
                    accountService.signOut()
                }
            }

            is ProfileEvent.updateAvatar -> {
                viewModelScope.launch(ioDispatcher) {
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