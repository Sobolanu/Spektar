package com.example.spektar.ui.archiveScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.spektar.data.local.ArchiveDatabase
import com.example.spektar.data.local.dao.ArchiveDao
import com.example.spektar.data.model.roomModels.Media
import com.example.spektar.data.model.roomModels.toSpecificMedia
import com.example.spektar.domain.model.SpecificMedia
import com.example.spektar.domain.model.services.AccountService
import com.example.spektar.domain.model.services.MediaService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class DailyGoal(
    val daily_goal_set : Boolean = false,
    val dailyGoal : Int = 0, // as in, amount you want to watch/read
    val totalSize : Int = 0 // as in, num of pages or episodes
)

class ArchiveViewModel (
    private val archiveDao: ArchiveDao,
    private val mediaService: MediaService,
    private val accountService: AccountService
) : ViewModel() {
    /* val archivedMedias: StateFlow<List<SpecificMedia>> = archiveDao.getAllArchivedMedia().map { list
        -> list.map { it.toSpecificMedia() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList()) */

    val archivedMedias: StateFlow<List<Media>> = archiveDao.getAllArchivedMedia()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val _dailyGoalState = MutableStateFlow(DailyGoal())
    val dailyGoalState = _dailyGoalState.asStateFlow()

    fun onEvent(event: ArchiveEvent) {
        when(event) {
            is ArchiveEvent.saveMedia -> {
                viewModelScope.launch {
                    archiveDao.insertMediaToArchive(event.media)
                }
            }

            is ArchiveEvent.removeMedia -> {
                viewModelScope.launch {
                    // archiveDao.removeMediaFromArchive(event.media.toMedia())
                }
            }

            is ArchiveEvent.mediaReview -> {
                viewModelScope.launch {
                    val userId = accountService.retrieveUserId()
                    userId.fold(
                        ifLeft = {
                            // error :)
                        },

                        ifRight = { id ->
                            mediaService.leaveReview(
                                mediaId = event.mediaId,
                                review = event.rating,
                                message = event.message,
                                userId = id
                            )
                        }
                    )
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ArchiveViewModelFactory(
    private val context: Context,
    private val mediaService: MediaService,
    private val accountService: AccountService
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = Room.databaseBuilder(
            context.applicationContext,
            ArchiveDatabase::class.java,
            "archives.db"
        ).build()

        return ArchiveViewModel(db.archiveDao, mediaService, accountService) as T
    }
}