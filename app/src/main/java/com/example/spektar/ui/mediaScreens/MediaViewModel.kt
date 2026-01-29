package com.example.spektar.ui.mediaScreens
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import arrow.core.Either
import com.example.spektar.data.model.media.MediaPreview
import com.example.spektar.data.model.roomModels.Media
import com.example.spektar.data.remote.authService.SessionFailure
import com.example.spektar.data.remote.mediaService.FullMediaData
import com.example.spektar.data.remote.mediaService.ReviewData
import com.example.spektar.data.repository.globalCategoryList
import com.example.spektar.domain.model.SpecificMedia
import com.example.spektar.domain.model.services.AccountService
import com.example.spektar.domain.model.services.MediaService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MediaViewModel (
    private val mediaService: MediaService,
    private val accountService: AccountService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private val _search = MutableStateFlow(emptyList<MediaPreview>())
    val search: StateFlow<List<MediaPreview>> = _search.asStateFlow()
    private val _media = MutableStateFlow<SpecificMedia?>(null)
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    private val _recommendedMedia = MutableStateFlow<List<List<MediaPreview>?>>(List(4) { emptyList() })
    private val _snackbarText = MutableStateFlow<String?>(null)

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _reviews = MutableStateFlow<List<ReviewData>>(emptyList())
    val reviews : StateFlow<List<ReviewData>> = _reviews.asStateFlow()

    fun onQueryChanged(q: String) {
        _query.value = q
    }

    val uiState = combine(_search, _categories, _recommendedMedia, _media, _snackbarText) { search, categories, recommendedMedia, media, snackbarText ->
        MediaUiState(
            medias = recommendedMedia,
            searchMedias = search,
            categories = categories,
            media = media,
            snackBarText = snackbarText
        )
    }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = MediaUiState())

    @OptIn(FlowPreview::class)
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
                onQueryChanged(event.name)
            }

            is MediaEvent.ReviewsForMedia -> {
                viewModelScope.launch {
                    val list = withContext(ioDispatcher) {
                        mediaService.fetchReviews(event.mediaId) // suspend call
                    }
                    _reviews.value = list
                }
            }
        }
    }

    init {
        _query
            .debounce(300)
            .map { it.trim() }
            .distinctUntilChanged()
            .flatMapLatest { q ->
                if (q.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    flow {
                        val result = mediaService.searchByName(q)
                        emit(result)
                    }.flowOn(ioDispatcher)
                        .catch { emit(emptyList()) }
                }
            }
            .onEach { results -> _search.value = results }
            .launchIn(viewModelScope) // non-blocking, runs concurrently

        viewModelScope.launch(ioDispatcher) {
            try {
                _categories.value = mediaService.getAllCategories()
            } catch (t: Throwable) {
                // handle or log; don't crash other collectors
                _snackbarText.value = "Failed to load categories"
            }
        }

        viewModelScope.launch {
            accountService.sessionFlow
                .filterNotNull()
                .collectLatest { session ->
                    when (val idResult = accountService.retrieveUserId()) {
                        is Either.Left -> {
                            val failure = idResult.value
                            val errorMessage = when (failure) {
                                is SessionFailure.SessionNotFound -> "Session not found, please log in again."
                                is SessionFailure.SessionExpired -> "Session expired, please log in again."
                                is SessionFailure.RequestTimeout -> "Request timed out, please retry."
                                else -> "Unknown authentication error."
                            }
                            _snackbarText.value = errorMessage
                        }
                        is Either.Right -> {
                            val userId = idResult.value
                            viewModelScope.launch(ioDispatcher) {
                                try {
                                    val recommended = globalCategoryList.map { category ->
                                        mediaService.fillCategory(
                                            session.accessToken,
                                            userId,
                                            category.mediaCategory.lowercase()
                                        )
                                    }
                                    _recommendedMedia.value = recommended
                                } catch (t: Throwable) {
                                    _snackbarText.value = "Failed to load recommendations"
                                }
                            }
                        }
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