package com.example.spektar.viewmodels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.spektar.models.Category
import com.example.spektar.models.CategoryRepository
import com.example.spektar.models.MediaRepository
import com.example.spektar.models.SpecificMedia

/* TODO:
    - clean up MediaUiState, have it only contain media and categories,
    as a result - in CategoryPageScreen.kt and MediaDetails.kt, figure out
    a cleaner way to load all the text and images necessary.
 */

/*
defines some properties for a uiState variable
*/

data class MediaUiState (
    val media: List<SpecificMedia> = emptyList(),
    val categories: List<Category> = emptyList(),
    val categoryNames: List<String> = emptyList(),
    val mediaImageURLs : List<String> = emptyList(),
    val mediaDescriptions : List<String> = emptyList(),
)

class MediaViewModel(
    private val mediaRepository: MediaRepository = MediaRepository(),
    private val categoryRepository: CategoryRepository = CategoryRepository()
) : ViewModel() {

    /*
    * Setter and getter for a uiState variable (_uiState is it's backing field.)
    * LiveData is a data holder class that can be observed with Observers and
    * maintains all data inside of it until a lifecycle is considered DESTROYED.
    * Anything that is LiveData will maintain it's data even through stuff like screen rotations.
    */

    private val _uiState = MutableLiveData(MediaUiState())
    val uiState: LiveData<MediaUiState> get() = _uiState

    init {
        loadData()
    }

    /*
        uiState obtains all values that are stored in the repositories.
    */
    private fun loadData() {
        val media = mediaRepository.getMedia()
        val categories = categoryRepository.getCategories()

        _uiState.value = MediaUiState(
            media = media,
            categories = categories, // name of each specific thing
            categoryNames = categories.map { it.mediaCategory }, // name of category each specific thing belongs to
            mediaImageURLs = media.map {it.mediaImageURL},
            mediaDescriptions = media.map {it.specificMediaDescription}
        )
    }
}
