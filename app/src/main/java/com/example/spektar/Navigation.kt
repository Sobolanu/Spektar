package com.example.spektar

import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.spektar.errorHandling.AppErrorScreen
import com.example.spektar.models.DataStoreViewModel
import com.example.spektar.screens.homeScreen.HomeScreen
import com.example.spektar.screens.settingsScreen.accessibilityScreen.AccessibilityScreen
import com.example.spektar.screens.mediaCategories.CategoryScreen
import com.example.spektar.screens.userScreens.CreateUserScreen
import com.example.spektar.screens.mediaDetails.MediaDetailsScreen
import com.example.spektar.screens.userScreens.UserRegistrationScreen
import com.example.spektar.screens.mediaCategories.MediaViewModel
import com.example.spektar.screens.settingsScreen.Access
import com.example.spektar.screens.settingsScreen.SettingsScreen
import com.example.spektar.screens.settingsScreen.themeScreen.ThemeScreen
import com.example.spektar.screens.userScreens.SignInViewModel
import com.example.spektar.screens.userScreens.SignUpViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

/*
Navigation uses "modern" (used to be modern, however Navigation3 came out but i'm kinda crunched on time so
i don't have time to migrate to Navigation3) type-safe navigation, which is also quite easy to work with.
 */

// TODO: SharedPreferences for maintaining state of stuff even on app close

@Composable
fun SpektarNavigation(
    mediaViewModel : MediaViewModel,
    signInViewModel : SignInViewModel,
    signUpViewModel : SignUpViewModel,
    dataStoreViewModel : DataStoreViewModel
) {
    val navController = rememberNavController()
    var selectedIcon = 0 // used to specify the currently selected icon in the app's bottom bar

    NavHost(navController = navController, startDestination = UserLoginScreen) {
        composable<CategoryScreen> {
            CategoryScreen(
                onImageClick = { position ->
                    navController.navigate(MediaDetails(
                        position.indexCategory,
                        position.mediaIndexInsideOfCategory // integrate color based off of categoryColor
                    ))
                },

                onBottomBarItemClick = { index ->
                    selectedIcon = index
                    bottomBarNavigation(navController, index)
                },

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

        // This composable is not implemented yet on the "actually getting errors" front
        composable<AppErrorScreen>(
            typeMap = mapOf(typeOf<AppErrorScreen>() to navTypeOf<AppErrorScreen>())
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<AppErrorScreen>()
            AppErrorScreen(
                errorMessage = args.errorMessage
            )
        }

        navigation<Settings>(startDestination = SettingsScreen) { // nested graph responsible for everything on settings page
            composable<SettingsScreen> {
                SettingsScreen(
                    navigateToScreen = { id ->
                        navController.navigate(
                            // placeholder until i sort out error messaging
                                when (id) {
                                    Access.THEME_SCREEN.ordinal -> ThemeScreen
                                    Access.ACCESSIBILITY_SCREEN.ordinal -> AccessibilityScreen
                                    Access.PROFILE_SETTINGS_SCREEN.ordinal -> ProfileSettingsScreen
                                    Access.HELP_SUPPORT_SCREEN.ordinal -> HelpSupportScreen
                                    Access.DONATE_SCREEN.ordinal -> DonateScreen
                                    else -> { AppErrorScreen } // implement error screen when you get around to it
                                }

                        )
                    },

                    onBottomBarItemClick = { index ->
                        selectedIcon = index
                        bottomBarNavigation(navController, index)
                    },

                    selectedIcon = selectedIcon
                )
            }

            composable<ThemeScreen> {
                ThemeScreen(
                    onBottomBarItemClick = { index ->
                        selectedIcon = index
                        bottomBarNavigation(navController, index)
                    },

                    selectedIcon = selectedIcon,
                    viewModel = dataStoreViewModel
                )
            }

            composable<AccessibilityScreen> {
                AccessibilityScreen(
                    onBottomBarItemClick = { index ->
                        selectedIcon = index
                        bottomBarNavigation(navController, index)
                    },

                    selectedIcon = selectedIcon
                )
            }

            composable<HelpSupportScreen> {
                HomeScreen() // placeholder
            }

            composable<DonateScreen> {
                HomeScreen() // placeholder
            }
        }
    }
}

fun bottomBarNavigation( // move someplace else when you re-organize your project eventually
    navController : NavController,
    index : Int
) {
    navController.navigate(when (index) {
        0 -> Settings // all are Settings because i haven't made the rest yet
        1 -> Settings
        2 -> Settings
        else -> Settings
    })
}

// These are the routes that the navigation uses, defined as either objects or data classes depending on
// if the routes require data passed between them or not
@Serializable
data class MediaDetails(
    val indexCategory: Int,
    val mediaIndexInsideOfCategory: Int,
)
@Serializable
data class AppErrorScreen (
    val errorMessage: String
)

@Serializable object CategoryScreen
@Serializable object UserLoginScreen
@Serializable object UserRegistrationScreen
@Serializable object SettingsScreen
@Serializable object ThemeScreen
@Serializable object Settings
@Serializable object AccessibilityScreen
@Serializable object ProfileSettingsScreen
@Serializable object HelpSupportScreen
@Serializable object DonateScreen

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