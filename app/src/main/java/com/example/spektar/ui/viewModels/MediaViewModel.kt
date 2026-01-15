package com.example.spektar.ui.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.data.model.EdgeResponse
import com.example.spektar.data.model.SpecificMedia
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
    val medias: List<List<SpecificMedia>> = emptyList(),
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

    suspend fun fetchMedia(): EdgeResponse? {
        val session = accountService.retrieveSession()
        val userId = accountService.retrieveUserId()

        if(session != null) {
            return mediaService.fetchTopMediaMatches(
                edgeUrl = "https://rlyotyktmhyflfyljpmr.supabase.co/functions/v1/content-recommendation",
                bearerToken = session.accessToken,
                userId = userId
            )
        }

        Exception("fetchMedia failed to receive a session.")
        return null
    }

    // uiState obtains all values that are stored in the repositories.

    fun obtainAllImagesInCategory(categoryIndex : Int) : List<String> {
        return mediaService.obtainAllImagesInCategory(uiState.value.medias[categoryIndex])
    }

    fun obtainAllNamesInCategory(categoryIndex : Int) : List<String> {
        return mediaService.obtainAllNamesInCategory(uiState.value.medias[categoryIndex])
    }

    private fun loadData() {
        viewModelScope.launch {
            val categories = mediaService.getAllCategories()

            val shows = mediaService.fillCategory("shows")
            val books = mediaService.fillCategory("books")
            val games = mediaService.fillCategory("games")
            val movies = mediaService.fillCategory("movies")

            _uiState.value = MediaUiState(
                medias = listOf(shows, books, games, movies),
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
