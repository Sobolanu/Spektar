package com.example.spektar.ui.navigation.graphs

import com.example.spektar.data.model.roomModels.MediaId
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.spektar.domain.media.MediaPreview
import com.example.spektar.domain.media.SpecificMedia
import com.example.spektar.domain.model.Category
import com.example.spektar.ui.notesScreen.NoteScreen
import com.example.spektar.ui.common.ErrorScreen
import com.example.spektar.ui.mediaScreens.CategoryScreen
import com.example.spektar.ui.mediaScreens.MediaDetailsScreen
import com.example.spektar.ui.mediaScreens.MoreMedia
import com.example.spektar.ui.navigation.routes.AppErrorScreen
import com.example.spektar.ui.navigation.routes.CategoryScreen
import com.example.spektar.ui.navigation.routes.MediaDetails
import com.example.spektar.ui.navigation.routes.MoreMedia
import com.example.spektar.ui.navigation.routes.NoteScreen
import com.example.spektar.ui.navigation.routes.ProfileScreen
import com.example.spektar.ui.navigation.utils.navTypeOf
import com.example.spektar.ui.navigation.utils.safeNavigate
import com.example.spektar.ui.viewModels.MediaViewModel
import com.example.spektar.ui.viewModels.NoteViewModel
import kotlin.reflect.typeOf

fun NavGraphBuilder.CategoryGraph(
    navController: NavController,
    mediaViewModel: MediaViewModel,
    selectedIconProvider: () -> Int,
    onBottomBarClick: (Int) -> Unit,
    noteViewModel: NoteViewModel,
) {
    composable<CategoryScreen> {
        val state = mediaViewModel.uiState.collectAsState()
        CategoryScreen(

            goToProfile = { navController.safeNavigate(ProfileScreen) },
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

        LaunchedEffect(args.partialMediaData) {
            state = mediaViewModel.obtainMediaById(args.partialMediaData)
        }

        MediaDetailsScreen(
            goToProfile = { navController.safeNavigate(ProfileScreen) },
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

        LaunchedEffect(args.id) {
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

    // This composable is not implemented yet on the "actually getting errors" front
    // you should probably move this somewhere other than here
    composable<AppErrorScreen>(
        typeMap = mapOf(typeOf<AppErrorScreen>() to navTypeOf<AppErrorScreen>())
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<AppErrorScreen>()
        ErrorScreen(args.errorMessage)
    }
}