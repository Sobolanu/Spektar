package com.example.spektar.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.spektar.ui.CategoryScreen
import com.example.spektar.ui.MediaDetailsScreen
import com.example.spektar.ui.common.ErrorScreen
import com.example.spektar.ui.navigation.navTypeUtils.navTypeOf
import com.example.spektar.ui.navigation.routes.AppErrorScreen
import com.example.spektar.ui.navigation.routes.CategoryScreen
import com.example.spektar.ui.navigation.routes.MediaDetails
import com.example.spektar.ui.viewModels.MediaViewModel
import kotlin.reflect.typeOf

fun NavGraphBuilder.CategoryGraph(
    navController: NavController,
    mediaViewModel: MediaViewModel,
    selectedIconProvider: () -> Int,
    onBottomBarClick: (Int) -> Unit
) {
    composable<CategoryScreen> {
        CategoryScreen(
            onImageClick = { media ->
                navController.navigate(
                    MediaDetails(
                        // indexOfCategory = mediaPosition.indexOfCategory,
                        // indexOfMediaInsideCategory = mediaPosition.indexOfMediaInsideCategory // integrate color based off of categoryColor
                        media.mediaId
                    )
                ) { launchSingleTop = true }
            },

            onBottomBarItemClick = onBottomBarClick,
            selectedIcon = selectedIconProvider(),
            viewModel = mediaViewModel
        )
    }

    composable<MediaDetails>(
        typeMap = mapOf(typeOf<MediaDetails>() to navTypeOf<MediaDetails>())
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<MediaDetails>()
        MediaDetailsScreen(
            onBackClick = { navController.popBackStack() },
            mediaPosition = args.mediaId, // Pair(args.indexOfCategory, args.indexOfMediaInsideCategory) ,
            viewModel = mediaViewModel
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