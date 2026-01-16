package com.example.spektar.ui.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.data.model.EdgeResponse
import com.example.spektar.data.model.MediaPreview
import com.example.spektar.data.model.SpecificMedia
import com.example.spektar.data.repository.globalCategoryList
import com.example.spektar.domain.model.AccountService
import com.example.spektar.domain.model.Category
import com.example.spektar.domain.model.MediaService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/*
    defines some properties for a uiState variable

    implement error handling when you have the energy to do so
*/

data class MediaUiState (
    val medias: List<List<MediaPreview>> = emptyList(),
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
    suspend fun obtainMediaById(id: String) : SpecificMedia {
        return mediaService.obtainDataByMediaId(id)
    }

    private fun loadData() {
        viewModelScope.launch {
            val mediaList = globalCategoryList.map {
                mediaService.fillCategory((it.mediaCategory[0].lowercase() + it.mediaCategory.substring(1)))
            }

            val categories = mediaService.getAllCategories()

            val session = accountService.retrieveSession()
            val userId = accountService.retrieveUserId()

            val recommendedMedia : EdgeResponse? = if(session != null) { // adapt so this fetches all media data?
                mediaService.fetchTopMediaMatches(
                    edgeUrl = "https://rlyotyktmhyflfyljpmr.supabase.co/functions/v1/content-recommendation",
                    bearerToken = session.accessToken,
                    userId = userId
                )
            } else {
                Exception("fetchMedia failed to receive a session.")
                null
            }

            _uiState.value = MediaUiState(
                medias = mediaList,
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
