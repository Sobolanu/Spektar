package com.example.spektar.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.models.AnimeRepository
import com.example.spektar.models.Category
import com.example.spektar.models.CategoryRepository
import com.example.spektar.models.SpecificMedia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import kotlin.collections.emptyList

/* TODO:
    - Adapt this ViewModel to use the data from Firebase. This requires:
    Cleaning up MediaUiState, have it only contain media and categories
    As a result - in CategoryPageScreen.kt and MediaDetails.kt, figure out
    a cleaner way to load all the text and images that are necessary.
 */

/*
defines some properties for a uiState variable
*/

data class MediaUiState (
    val medias: List<SpecificMedia> = emptyList(),
    val categories: List<Category> = emptyList(),
)

class MediaViewModel (
    private val animeRepository: AnimeRepository,
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(MediaUiState())
    val uiState: StateFlow<MediaUiState> get() = _uiState

    val allImagesInCategory : StateFlow<List<String>> =
        _uiState
            .map { uiState ->
            uiState.medias.map {media -> media.imageUrl}
        }
            .stateIn(scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList() )

    init {
        loadData()
    }

    // uiState obtains all values that are stored in the repositories.
    private fun loadData() {
        viewModelScope.launch() {
            val animes = animeRepository.getAllAnimes()
            val categories = categoryRepository.getCategories()

            _uiState.value = MediaUiState(
                medias = animes,
                // medias = medias,
                categories = categories, // name of each specific thing
            )
        }
    }
}

// figure out how to avoid using factories cause ts kinda ugly
class MediaViewModelFactory(
    private val animeRepository: AnimeRepository,
    private val categoryRepository: CategoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MediaViewModel::class.java)) {
            return MediaViewModel(animeRepository, categoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
