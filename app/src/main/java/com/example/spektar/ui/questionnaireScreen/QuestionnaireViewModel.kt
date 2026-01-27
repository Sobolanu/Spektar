package com.example.spektar.ui.questionnaireScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.data.model.User
import com.example.spektar.domain.model.services.AccountService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestionnaireViewModel(
   private val accountService: AccountService,
   private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    // empty embedding that gets filled out
    // empty embedding is val emptyEmbedding = List(47, { 0 })
    private var _embedding = MutableStateFlow(MutableList(47, {0}))
    val embedding : StateFlow<List<Int>> get() = _embedding

    val _userState = MutableStateFlow(User())
    val userState : StateFlow<User> get() = _userState

    init {
        viewModelScope.launch(ioDispatcher) {
            accountService.sessionFlow.collect { session ->
                if (session != null) {
                    val fullUserData = accountService.retrieveUserDataWithId(session.user!!.id)

                    _userState.value = User(
                        id = session.user!!.id,
                        username = fullUserData.username,
                        email =  session.user!!.email!!,
                        avatar_url = fullUserData.avatar_url?.let { accountService.storageUrl(it) },
                        accountCreationDate = session.user!!.createdAt.toString()
                    )
                } else {
                    Exception("Session is null at function loadData of QuestionnaireViewModel.")
                }
            }
        }
    }

    fun onEvent(event: QuestionnaireEvent) {
        when(event) {
            is QuestionnaireEvent.UpdateEmbedding -> {
                when(event.action) {
                    "Set" -> { // unique to age, don't modify
                        _embedding.update { current ->
                            current.toMutableList().apply {
                                for (i in 0..2) {
                                    this[i] = 0
                                }

                                this[event.indexToUpdate] = 1
                            }
                        }
                    }

                    "Add" -> {
                        _embedding.update { current ->
                            current.toMutableList().apply {
                                this[event.indexToUpdate] = 1
                            }
                        }
                    }

                    "Remove" -> {
                        _embedding.update { current ->
                            current.toMutableList().apply {
                                this[event.indexToUpdate] = 0
                            }
                        }
                    }
                }
            }

            is QuestionnaireEvent.FinishQuestionnaire -> {
                viewModelScope.launch(ioDispatcher) {
                    accountService.updateUserSuggestions(userState.value.id, embedding.value)
                }
            }
        }

        println(embedding.value)
    }
}

@Suppress("UNCHECKED_CAST")
class QuestionnaireViewModelFactory(
    private val accountService: AccountService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionnaireViewModel::class.java)) {
            return QuestionnaireViewModel(
                accountService = accountService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}