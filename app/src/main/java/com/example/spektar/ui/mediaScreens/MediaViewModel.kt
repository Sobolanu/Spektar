package com.example.spektar.ui.mediaScreens
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.spektar.data.model.media.MediaPreview
import com.example.spektar.data.remote.authService.SessionFailure
import com.example.spektar.data.remote.mediaService.FullMediaData
import com.example.spektar.data.repository.globalCategoryList
import com.example.spektar.domain.model.SpecificMedia
import com.example.spektar.domain.model.services.AccountService
import com.example.spektar.domain.model.services.MediaService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MediaViewModel (
    private val mediaService: MediaService,
    private val accountService: AccountService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _search = MutableStateFlow(emptyList<MediaPreview>())
    private val _media = MutableStateFlow<SpecificMedia?>(null)
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    private val _recommendedMedia = MutableStateFlow<List<List<MediaPreview>?>>(List(4) { emptyList() })

    private val _snackbarText = MutableStateFlow<String?>(null)
    val uiState = combine(_search, _categories, _recommendedMedia, _media, _snackbarText) { search, categories, recommendedMedia, media, snackbarText ->
        MediaUiState(
            medias = recommendedMedia,
            searchMedias = search,
            categories = categories,
            media = media,
            snackBarText = snackbarText
        )
    }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = MediaUiState())

    fun onEvent(event: MediaEvent) {
        when(event) {
            is MediaEvent.ObtainMediaById -> {
                viewModelScope.launch(ioDispatcher) {
                    val result = obtainMediaById(event.media)
                    _media.value = SpecificMedia(
                        id_uuid = result.id_uuid,
                        name = result.name,
                        imageUrl = result.imageUrl,
                        description = result.description,
                        credits = result.credits,
                        release_date = result.release_date
                    )
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
                    accountService.retrieveUserId().fold(
                        ifLeft = { failure ->
                            val errorMessage = when(failure) {
                                is SessionFailure.SessionNotFound -> "Session not found, please log in again."
                                is SessionFailure.SessionExpired -> "Session expired, please log in again."
                                is SessionFailure.RequestTimeout -> "Request timed out, please retry."
                                else -> {"Unknown authentication error."}
                            }

                            _snackbarText.value = errorMessage
                        },
                        ifRight = { success ->
                            val userId = success
                            val recommendedMedia = globalCategoryList.map {
                                mediaService.fillCategory(
                                    session.accessToken,
                                    userId,
                                    it.mediaCategory.lowercase()
                                )
                            }

                            _recommendedMedia.value = recommendedMedia
                        }
                    )
                }
            }
        }
    }


    // uiState obtains all values that are stored in the repositories.
    suspend fun obtainMediaById(partialMediaData: MediaPreview) : FullMediaData {
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