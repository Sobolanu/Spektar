package com.example.spektar.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.spektar.domain.model.services.MediaService
import kotlinx.coroutines.flow.MutableStateFlow

/*
q1:
0-12 kid
13-18 teen
18+ adult

my q2 would be asmr's q4 (sorts out general categories)
q3 could ask "Do you like anime?" and if yes, what type
q4 would ask "Do you like games?", if yes what type

 */
class QuestionnaireViewModel(
   private val mediaService: MediaService
) : ViewModel() {

    // empty embedding that gets filled out

    fun onEvent() {

    }
}

@Suppress("UNCHECKED_CAST")
class QuestionnaireViewModelFactory(
    private val mediaService: MediaService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionnaireViewModel::class.java)) {
            return QuestionnaireViewModel(
                mediaService = mediaService
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}