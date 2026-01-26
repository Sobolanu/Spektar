package com.example.spektar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.domain.model.services.AccountService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    accountService: AccountService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private val _userAuthenticated = MutableStateFlow(false)
    val userAuthenticated = _userAuthenticated.asStateFlow()

    init {
        viewModelScope.launch {
            if(accountService.retrieveSession() != null) {
                _userAuthenticated.value = true
            } else {
                _userAuthenticated.value = false
            }

            delay(3000)

            _isReady.value = true
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(
    private val accountService: AccountService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                accountService,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}