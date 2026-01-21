package com.example.spektar.ui.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.domain.media.MediaPreview
import com.example.spektar.domain.media.SpecificMedia
import com.example.spektar.data.model.viewModelStates.MediaUiData
import com.example.spektar.data.repository.globalCategoryList
import com.example.spektar.domain.model.AccountService
import com.example.spektar.domain.model.MediaService
import com.example.spektar.ui.mediaScreens.MediaEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


// DO NOT UNDER ANY CIRCUMSTANCES GET RID OF THE NON-EXPERIMENTAL FUNCTIONS
// WITHOUT THEM THE CODE BREAKS FOR SOME REASON AND I DON'T KNOW HOW TO FIX IT
/*
    defines some properties for a uiState variable

    implement error handling when you have the energy to do so
*/

class MediaViewModel (
    private val mediaService: MediaService,
    private val accountService: AccountService
) : ViewModel() {
    private val _uiState = MutableStateFlow(MediaUiData())
    val uiState: StateFlow<MediaUiData> get() = _uiState
    private val _media = MutableStateFlow<SpecificMedia?>(null)
    val media: StateFlow<SpecificMedia?> = _media // combine this into uiState some time

    fun onEvent(event: MediaEvent) {
        when(event) {
            is MediaEvent.ObtainMediaById -> {
                viewModelScope.launch {
                    val result = obtainMediaById(event.media)
                    _media.value = result
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            accountService.sessionFlow.collect { session ->
                if (session != null) {
                    val userId = accountService.retrieveUserId()
                    val recommendedMedia = globalCategoryList.map {
                        mediaService.EXPERIMENTALfillCategory(
                            session.accessToken,
                            userId,
                            it.mediaCategory.lowercase()
                        )
                    }
                    _uiState.value = _uiState.value.copy(
                        medias = recommendedMedia
                    )
                }
            }
        }
        loadData() // optional initial load
    }


    // uiState obtains all values that are stored in the repositories.
    suspend fun obtainMediaById(partialMediaData: MediaPreview) : SpecificMedia {
        return mediaService.obtainDataByMediaId(partialMediaData)
    }

    private fun loadData() {
        viewModelScope.launch {
            // DO NOT UNDER ANY CIRCUMSTANCES GET RID OF THE NON-EXPERIMENTAL FUNCTIONS
            // WITHOUT THEM THE CODE BREAKS FOR SOME REASON AND I DON'T KNOW HOW TO FIX IT
            val mediaList = globalCategoryList.map {
                mediaService.fillCategory(it.mediaCategory.lowercase())
            }

            val categories = mediaService.getAllCategories()

            accountService.sessionFlow.collect { session ->
                if (session != null) {
                    val userId = accountService.retrieveUserId()
                    val recommendedMedia = globalCategoryList.map {
                        mediaService.EXPERIMENTALfillCategory(
                            session.accessToken,
                            userId,
                            it.mediaCategory.lowercase()
                        )
                    }
                    _uiState.value = _uiState.value.copy(
                        medias = recommendedMedia,
                        categories = categories,
                    )
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MediaViewModelFactory(
    private val mediaService: MediaService,
    private val accountService: AccountService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MediaViewModel::class.java)) {
            return MediaViewModel(
                mediaService,
                accountService,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}