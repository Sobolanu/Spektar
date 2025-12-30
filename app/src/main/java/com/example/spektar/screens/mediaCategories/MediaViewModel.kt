package com.example.spektar.screens.mediaCategories
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.models.repositories.BookRepository
import com.example.spektar.models.repositories.ShowRepository
import com.example.spektar.models.Category
import com.example.spektar.models.repositories.CategoryRepository
import com.example.spektar.models.repositories.GameRepository
import com.example.spektar.models.SpecificMedia
import com.example.spektar.models.repositories.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/*
    defines some properties for a uiState variable
*/

data class MediaUiState (
    val medias: List<List<SpecificMedia>> = emptyList(),
    val categories: List<Category> = emptyList(),
)

class MediaViewModel (
    private val categoryRepository: CategoryRepository,

    private val showRepository: ShowRepository,
    private val bookRepository : BookRepository,
    private val gameRepository: GameRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MediaUiState())
    val uiState: StateFlow<MediaUiState> get() = _uiState

    init {
        loadData()
    }

    // uiState obtains all values that are stored in the repositories.
    private fun loadData() {
        viewModelScope.launch {
            val categories = categoryRepository.getAllCategories()

            val shows = showRepository.getAllShows()
            val books = bookRepository.getAllBooks()
            val games = gameRepository.getAllGames()
            val movies = movieRepository.getAllMovies()

            _uiState.value = MediaUiState(
                medias = listOf(shows, books, games, movies),
                categories = categories, // name of each specific thing
            )
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MediaViewModelFactory(
    private val categoryRepository: CategoryRepository,

    private val showRepository: ShowRepository,
    private val bookRepository : BookRepository,
    private val gameRepository: GameRepository,
    private val movieRepository: MovieRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MediaViewModel::class.java)) {
            return MediaViewModel(
                categoryRepository,

                showRepository,
                bookRepository,
                gameRepository,
                movieRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
