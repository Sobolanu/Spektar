package com.example.spektar.ui.navigation.graphs.categoryGraph

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.example.spektar.data.local.NoteDatabase
import com.example.spektar.data.local.dao.MediaDao
import com.example.spektar.data.local.dao.NoteDao
import com.example.spektar.data.model.media.MediaPreview
import com.example.spektar.data.model.roomModels.MediaId
import com.example.spektar.data.remote.AccountServiceImpl
import com.example.spektar.data.remote.MediaServiceImpl
import com.example.spektar.domain.model.SpecificMedia
import com.example.spektar.ui.mediaScreens.Category
import com.example.spektar.ui.mediaScreens.CategoryScreen
import com.example.spektar.ui.mediaScreens.MediaDetailsScreen
import com.example.spektar.ui.mediaScreens.MediaViewModel
import com.example.spektar.ui.mediaScreens.MediaViewModelFactory
import com.example.spektar.ui.mediaScreens.MoreMedia
import com.example.spektar.ui.navigation.graphs.common.ProfileScreen
import com.example.spektar.ui.navigation.utils.navTypeOf
import com.example.spektar.ui.navigation.utils.safeNavigate
import com.example.spektar.ui.notesScreen.NoteScreen
import com.example.spektar.ui.notesScreen.NoteViewModel
import com.example.spektar.ui.notesScreen.NoteViewModelFactory
import java.security.AccessController.getContext
import kotlin.getValue
import kotlin.reflect.typeOf

fun NavGraphBuilder.CategoryGraph(
    navController: NavController,
    selectedIconProvider: () -> Int,
    onBottomBarClick: (Int) -> Unit,
) {
    navigation<Media>(startDestination = CategoryScreen) {
        composable<CategoryScreen> { backStackEntry ->
            val mediaViewModel : MediaViewModel = viewModel<MediaViewModel> (
                viewModelStoreOwner = backStackEntry,
                factory = MediaViewModelFactory(MediaServiceImpl(), AccountServiceImpl()),
            )
            val state = mediaViewModel.uiState.collectAsState()

            CategoryScreen(
                onEvent = { event ->
                    mediaViewModel.onEvent(event)
                },
                // this must go to profile
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
            val args = backStackEntry.toRoute<MediaDetails>()

            var state by remember { mutableStateOf(SpecificMedia(
                id_uuid = args.partialMediaData.id_uuid,
                name = args.partialMediaData.name,
                imageUrl = args.partialMediaData.imageUrl
            )) }

            val mediaViewModel : MediaViewModel = viewModel<MediaViewModel> (
                viewModelStoreOwner = backStackEntry,
                factory = MediaViewModelFactory(MediaServiceImpl(), AccountServiceImpl()),
            )

            LaunchedEffect(args.partialMediaData) { // best loaded in init{} block?
                state = mediaViewModel.obtainMediaById(args.partialMediaData)
            }

            MediaDetailsScreen(
                goToProfile = { navController.safeNavigate(MoreMedia) },
                onBackClick = { navController.popBackStack() },
                onNoteButtonClick = {
                    navController.safeNavigate(NoteScreen(it)) // pass id here
                },
                state = state
            )
        }

        composable<MoreMedia>(
            typeMap = mapOf(typeOf<Category>() to navTypeOf<Category>())
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<MoreMedia>()

            val mediaViewModel : MediaViewModel = viewModel<MediaViewModel> (
                viewModelStoreOwner = backStackEntry,
                factory = MediaViewModelFactory(MediaServiceImpl(), AccountServiceImpl()),
            )

            val state = mediaViewModel.uiState.collectAsState()

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

            LaunchedEffect(args.id) { // // best loaded in init{} block?
                noteViewModel.setMedia(args.id)
            }

            val state = noteViewModel.state.collectAsState()
            NoteScreen(
                state = state.value,
                mediaId = args.id,
                onEvent = { event, mediaId ->
                    noteViewModel.onEvent(event, mediaId)
                }
            )
        }
    }
}