@file:Suppress("unused")

package com.example.spektar

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.spektar.models.BookRepository
import com.example.spektar.models.CategoryRepository
import com.example.spektar.models.GameRepository
import com.example.spektar.models.ShowRepository
import com.example.spektar.screens.CategoryScreen
import com.example.spektar.screens.MediaDetailsScreen
import com.example.spektar.ui.theme.SpektarTheme
import com.example.spektar.viewmodels.MediaViewModel
import com.example.spektar.viewmodels.MediaViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

object FirestoreManager {
    val db: FirebaseFirestore by lazy { Firebase.firestore }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpektarTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = CategoryScreen) {
                    composable<CategoryScreen> {
                        val viewModel: MediaViewModel by viewModels {
                            MediaViewModelFactory(
                                CategoryRepository(),

                                ShowRepository(FirebaseFirestore.getInstance()),
                                BookRepository(FirebaseFirestore.getInstance()),
                                GameRepository(FirebaseFirestore.getInstance())
                            )
                        }

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

                        val viewModel: MediaViewModel by viewModels {
                            MediaViewModelFactory(
                                CategoryRepository(),

                                ShowRepository(FirebaseFirestore.getInstance()),
                                BookRepository(FirebaseFirestore.getInstance()),
                                GameRepository(FirebaseFirestore.getInstance())
                            )
                        }

                        MediaDetailsScreen(
                            onBackClick = { navController.popBackStack() },
                            mediaPosition = Pair(args.indexCategory, args.mediaIndexInsideOfCategory),
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@Serializable
object CategoryScreen
@Serializable
data class MediaDetails(
    val indexCategory: Int,
    val mediaIndexInsideOfCategory: Int
)

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