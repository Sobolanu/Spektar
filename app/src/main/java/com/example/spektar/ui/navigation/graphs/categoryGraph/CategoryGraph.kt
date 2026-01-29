package com.example.spektar.ui.navigation.graphs.categoryGraph

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.spektar.data.model.media.MediaPreview
import com.example.spektar.data.model.roomModels.MediaId
import com.example.spektar.data.remote.authService.AccountServiceImpl
import com.example.spektar.data.remote.mediaService.FullMediaData
import com.example.spektar.data.remote.mediaService.MediaServiceImpl
import com.example.spektar.ui.archiveScreen.ArchiveEvent
import com.example.spektar.ui.archiveScreen.ArchiveScreen
import com.example.spektar.ui.archiveScreen.ArchiveViewModel
import com.example.spektar.ui.archiveScreen.ArchiveViewModelFactory
import com.example.spektar.ui.mediaScreens.Category
import com.example.spektar.ui.mediaScreens.CategoryScreen
import com.example.spektar.ui.mediaScreens.MediaDetailsScreen
import com.example.spektar.ui.mediaScreens.MediaEvent
import com.example.spektar.ui.mediaScreens.MediaViewModel
import com.example.spektar.ui.mediaScreens.MoreMedia
import com.example.spektar.ui.navigation.graphs.common.ProfileScreen
import com.example.spektar.ui.navigation.utils.navTypeOf
import com.example.spektar.ui.navigation.utils.safeNavigate
import com.example.spektar.ui.notesScreen.NoteScreen
import com.example.spektar.ui.notesScreen.NoteViewModel
import com.example.spektar.ui.notesScreen.NoteViewModelFactory
import kotlin.reflect.typeOf

fun NavGraphBuilder.CategoryGraph(
    navController: NavController,
    selectedIconProvider: () -> Int,
    onBottomBarClick: (Int) -> Unit,
    mediaViewModel: MediaViewModel
) {
    navigation<Media>(startDestination = CategoryScreen) {
        composable<CategoryScreen> {
            val state = mediaViewModel.uiState.collectAsStateWithLifecycle()

            CategoryScreen(
                onEvent = { event ->
                    mediaViewModel.onEvent(event)
                },
                goToProfile = { navController.safeNavigate(ProfileScreen)  },
                onImageClick = { media ->
                    navController.safeNavigate(
                        MediaDetails(media.partialMediaData)
                    )
                },

                onBottomBarItemClick = onBottomBarClick,
                selectedIcon = selectedIconProvider(),
                onMoreClick = { category ->
                    navController.safeNavigate(MoreMedia(category)) },
                state = state.value
            )
        }

        composable<MediaDetails>(
            typeMap = mapOf(typeOf<MediaPreview>() to navTypeOf<MediaPreview>())
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(Media::class)}
            val args = backStackEntry.toRoute<MediaDetails>()
            val context = LocalContext.current

            var state by remember { mutableStateOf(FullMediaData(
                id_uuid = args.partialMediaData.id_uuid,
                name = args.partialMediaData.name,
                imageUrl = args.partialMediaData.imageUrl
            )) }

            LaunchedEffect(args.partialMediaData) {
                state = mediaViewModel.obtainMediaById(args.partialMediaData)
            }

            val archiveViewModel : ArchiveViewModel = viewModel<ArchiveViewModel> (
                viewModelStoreOwner = parentEntry,
                factory = ArchiveViewModelFactory(
                    context,
                    MediaServiceImpl(),
                    AccountServiceImpl(),
                )
            )

            val isArchived = archiveViewModel.archivedMedias.value.any {
                it.name == state.name
            }

            val reviews = mediaViewModel.reviews.collectAsStateWithLifecycle()

            LaunchedEffect(reviews) {
                mediaViewModel.onEvent(MediaEvent.ReviewsForMedia(state.id_uuid))
            }

            MediaDetailsScreen(
                goToProfile = { navController.safeNavigate(MoreMedia) },
                onBackClick = { navController.popBackStack() },
                onNoteButtonClick = {
                    navController.safeNavigate(NoteScreen(it)) // pass id here
                },

                state = state,

                saveMedia = { media ->
                    archiveViewModel.onEvent(ArchiveEvent.saveMedia(media))
                },

                leaveReview = { mediaId, rating, message ->
                    archiveViewModel.onEvent(ArchiveEvent.mediaReview(
                        mediaId = mediaId,
                        rating = rating,
                        message = message
                    ))
                },
                isArchived = isArchived,
                reviews = reviews.value
            )
        }

        composable<MoreMedia>(
            typeMap = mapOf(typeOf<Category>() to navTypeOf<Category>())
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<MoreMedia>()

            val state = mediaViewModel.uiState.collectAsStateWithLifecycle()

            MoreMedia(
                goToProfile = { navController.safeNavigate(ProfileScreen) },
                onBottomBarItemClick = onBottomBarClick,
                onImageClick = { media ->
                    navController.safeNavigate(
                        MediaDetails(media.partialMediaData)
                    )
                },
                selectedIcon = selectedIconProvider(),
                category = args.category,
                state = state.value
            )
        }

        composable<NoteScreen>(
            typeMap = mapOf(typeOf<MediaId>() to navTypeOf<MediaId>())
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<NoteScreen>() // gets id
            val context = LocalContext.current

            val noteViewModel : NoteViewModel = viewModel<NoteViewModel> (
                viewModelStoreOwner = backStackEntry,
                factory = NoteViewModelFactory(context)
            )

            LaunchedEffect(args.id) {
                noteViewModel.setMedia(args.id)
            }

            val state = noteViewModel.state.collectAsStateWithLifecycle()
            NoteScreen(
                state = state.value,
                mediaId = args.id,
                onEvent = { event, mediaId ->
                    noteViewModel.onEvent(event, mediaId)
                }
            )
        }

        composable<ArchiveScreen> { backStackEntry ->
            val parentEntry = remember(backStackEntry) { navController.getBackStackEntry(Media::class)}

            val context = LocalContext.current
            val archiveViewModel : ArchiveViewModel = viewModel<ArchiveViewModel> (
                viewModelStoreOwner = parentEntry,
                factory = ArchiveViewModelFactory(
                    context,
                    MediaServiceImpl(),
                    AccountServiceImpl(),
                )
            )

            val state = archiveViewModel.archivedMedias.collectAsStateWithLifecycle()

            ArchiveScreen(
                goToProfile = { navController.safeNavigate(ProfileScreen) },
                onBottomBarItemClick = onBottomBarClick,
                state = state.value,
                selectedIcon = selectedIconProvider(),
                onImageClick = { media ->
                    navController.safeNavigate(
                        MediaDetails(media.partialMediaData)
                    )
                },
            )
        }
    }
}