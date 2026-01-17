package com.example.spektar.ui.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.data.model.EdgeResponse
import com.example.spektar.data.model.media.MediaPreview
import com.example.spektar.data.model.media.SpecificMedia
import com.example.spektar.data.repository.globalCategoryList
import com.example.spektar.domain.model.AccountService
import com.example.spektar.domain.model.Category
import com.example.spektar.domain.model.MediaService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


// DO NOT UNDER ANY CIRCUMSTANCES GET RID OF THE NON-EXPERIMENTAL FUNCTIONS
// WITHOUT THEM THE CODE BREAKS FOR SOME REASON AND I DON'T KNOW HOW TO FIX IT
/*
    defines some properties for a uiState variable

    implement error handling when you have the energy to do so
*/

data class MediaUiState (
    val medias: List<List<MediaPreview>?> = emptyList(),
    val categories: List<Category> = emptyList(),
)

class MediaViewModel (
    private val mediaService: MediaService,
    private val accountService: AccountService
) : ViewModel() {
    private val _uiState = MutableStateFlow(MediaUiState())
    val uiState: StateFlow<MediaUiState> get() = _uiState

    init {
        loadData()
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
            val session = accountService.retrieveSession()
            val userId = accountService.retrieveUserId()

            var recommendedMedia : List<List<MediaPreview>?> = emptyList()
            if(session != null) {
                // WORKS:
                // val testData = mediaService.fetchTopMediaMatches(session.accessToken, userId)

                // works (and i hope it will consistently)
                recommendedMedia = globalCategoryList.map {
                    mediaService.EXPERIMENTALfillCategory(session.accessToken, userId, it.mediaCategory.lowercase())
                }
            }

            _uiState.value = MediaUiState(
                // medias = mediaList,
                medias = recommendedMedia,
                categories = categories, // name of each specific thing
            )
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