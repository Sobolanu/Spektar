package com.example.spektar.ui.mediaScreens
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.data.model.media.MediaPreview
import com.example.spektar.data.repository.globalCategoryList
import com.example.spektar.domain.model.SpecificMedia
import com.example.spektar.domain.model.services.AccountService
import com.example.spektar.domain.model.services.MediaService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


// DO NOT UNDER ANY CIRCUMSTANCES GET RID OF THE NON-EXPERIMENTAL FUNCTIONS
// WITHOUT THEM THE CODE BREAKS FOR SOME REASON AND I DON'T KNOW HOW TO FIX IT
/*
    defines some properties for a uiState variable

    implement error handling when you have the energy to do so
*/

class MediaViewModel (
    private val mediaService: MediaService,
    private val accountService: AccountService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _search = MutableStateFlow(emptyList<MediaPreview>())
    private val _media = MutableStateFlow<SpecificMedia?>(null)
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    private val _recommendedMedia = MutableStateFlow<List<List<MediaPreview>?>>(List(4) { emptyList() })
    val uiState = combine(_search, _categories, _recommendedMedia, _media) { search, categories, recommendedMedia, media ->
        MediaUiState(
            medias = recommendedMedia,
            searchMedias = search,
            categories = categories,
            media = media,
        )
    }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = MediaUiState())

    fun onEvent(event: MediaEvent) {
        when(event) {
            is MediaEvent.ObtainMediaById -> {
                viewModelScope.launch(ioDispatcher) {
                    val result = obtainMediaById(event.media)
                    _media.value = result
                }
            }

            is MediaEvent.SearchForMedia -> {
                viewModelScope.launch(ioDispatcher) {
                    val result = mediaService.searchByName(event.name)
                    _search.value = result
                }
            }
        }
    }

    init {
        viewModelScope.launch(ioDispatcher) {
            _categories.value = mediaService.getAllCategories()

            accountService.sessionFlow.collectLatest { session ->
                if (session != null) {
                    val userId = accountService.retrieveUserId()
                    val recommendedMedia = globalCategoryList.map {
                        mediaService.fillCategory(
                            session.accessToken,
                            userId,
                            it.mediaCategory.lowercase()
                        )
                    }

                    _recommendedMedia.value = recommendedMedia
                }
            }
        }
    }


    // uiState obtains all values that are stored in the repositories.
    suspend fun obtainMediaById(partialMediaData: MediaPreview) : SpecificMedia {
        return mediaService.obtainDataByMediaId(partialMediaData)
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