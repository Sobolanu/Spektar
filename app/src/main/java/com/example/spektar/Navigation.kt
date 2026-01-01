package com.example.spektar

import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.spektar.screens.mediaCategories.CategoryScreen
import com.example.spektar.screens.userScreens.CreateUserScreen
import com.example.spektar.screens.mediaDetails.MediaDetailsScreen
import com.example.spektar.screens.userScreens.UserRegistrationScreen
import com.example.spektar.screens.mediaCategories.MediaViewModel
import com.example.spektar.screens.userScreens.SignInViewModel
import com.example.spektar.screens.userScreens.SignUpViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

/*
Navigation uses "modern" (used to be modern, however Navigation3 came out but i'm kinda crunched on time so
i don't have time to migrate to Navigation3) type-safe navigation, which is also quite easy to work with.
 */
@Composable
fun SpektarNavigation(
    mediaViewModel : MediaViewModel = viewModel(),
    signInViewModel : SignInViewModel = viewModel(),
    signUpViewModel : SignUpViewModel = viewModel()
) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = CategoryScreen) {
        composable<CategoryScreen> {
            CategoryScreen(
                onImageClick = { position ->
                    navController.navigate(MediaDetails(
                        position.indexCategory,
                        position.mediaIndexInsideOfCategory
                    ))
                },
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

        composable<UserLoginScreen> {
            CreateUserScreen(
                onSignInClick = { navController.navigate(CategoryScreen) }, // placeholder route until i make home screen
                onTextClick = { navController.navigate(UserRegistrationScreen) },
                viewModel = signInViewModel
            )
        }

        composable<UserRegistrationScreen> {
            UserRegistrationScreen(
                viewModel = signUpViewModel,
                onSignUp = { navController.navigate(CategoryScreen) } // read comment above
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
object UserLoginScreen

@Serializable
object UserRegistrationScreen

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