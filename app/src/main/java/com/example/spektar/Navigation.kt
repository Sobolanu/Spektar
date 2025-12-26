package com.example.spektar

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.spektar.screens.CategoryScreen
import com.example.spektar.screens.CreateUserScreen
import com.example.spektar.screens.MediaDetailsScreen
import com.example.spektar.viewmodels.MediaViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Composable
fun SpektarNavigation(
    viewModel : MediaViewModel = viewModel(),
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = UserCreateScreen) {
        composable<CategoryScreen> {
            CategoryScreen(
                onImageClick = { position ->
                    navController.navigate(MediaDetails(
                        position.indexCategory,
                        position.mediaIndexInsideOfCategory
                    ))
                },
                viewModel = viewModel
            )
        }

        composable<MediaDetails>(
            typeMap = mapOf(typeOf<MediaDetails>() to navTypeOf<MediaDetails>())
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<MediaDetails>()

            MediaDetailsScreen(
                onBackClick = { navController.popBackStack() },
                mediaPosition = Pair(args.indexCategory, args.mediaIndexInsideOfCategory),
                viewModel = viewModel
            )
        }

        composable<UserCreateScreen> {
            CreateUserScreen(
                onTextClick = { navController.navigate(CategoryScreen) }
            )
        }
    }
}

// These are the routes that the navigation uses, defined as either objects or data classes depending on
// if the routes require data passed between them or not
@Serializable
object CategoryScreen
@Serializable
data class MediaDetails(
    val indexCategory: Int,
    val mediaIndexInsideOfCategory: Int
)

@Serializable
object UserCreateScreen

// Function that turns a certain type T into a NavType for use in the navigation
inline fun <reified T> navTypeOf(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? =
        bundle.getString(key)?.let(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(Uri.decode(value))

    override fun serializeAsValue(value: T): String = Uri.encode(json.encodeToString(value))

    override fun put(bundle: Bundle, key: String, value: T) =
        bundle.putString(key, json.encodeToString(value))
}