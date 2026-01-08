package com.example.spektar.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.spektar.ui.CategoryScreen
import com.example.spektar.ui.MediaDetailsScreen
import com.example.spektar.ui.common.ErrorScreen
import com.example.spektar.ui.navigation.bottomBarNavigation.bottomBarNavigation
import com.example.spektar.ui.navigation.navTypeUtils.navTypeOf
import com.example.spektar.ui.navigation.routes.AppErrorScreen
import com.example.spektar.ui.navigation.routes.CategoryScreen
import com.example.spektar.ui.navigation.routes.MediaDetails
import com.example.spektar.ui.viewModels.MediaViewModel
import kotlin.reflect.typeOf

fun NavGraphBuilder.CategoryGraph(
    navController: NavController,
    mediaViewModel: MediaViewModel,
    selectedIcon: Int,
    onBottomBarClick: (Int) -> Unit
) {
    composable<CategoryScreen> {
        CategoryScreen(
            onImageClick = { mediaPosition ->
                navController.navigate(
                    MediaDetails(
                        indexCategory = mediaPosition.indexCategory,
                        mediaIndexInsideOfCategory = mediaPosition.mediaIndexInsideOfCategory // integrate color based off of categoryColor
                    )
                ) {launchSingleTop = true}
            },

            onBottomBarItemClick = onBottomBarClick,
            selectedIcon = selectedIcon,
            viewModel = mediaViewModel
        )
    }

    composable<MediaDetails>(
        typeMap = mapOf(typeOf<MediaDetails>() to navTypeOf<MediaDetails>())
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<MediaDetails>()
        MediaDetailsScreen(
            onBackClick = { navController.popBackStack() },
            mediaPosition = Pair(args.indexCategory, args.mediaIndexInsideOfCategory),
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