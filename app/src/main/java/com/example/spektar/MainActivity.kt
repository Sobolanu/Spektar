@file:Suppress("unused")

package com.example.spektar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.compose.SpektarTheme
import com.example.spektar.models.accountServices.AccountService
import com.example.spektar.models.accountServices.AccountServiceImpl
import com.example.spektar.models.repositories.BookRepository
import com.example.spektar.models.repositories.CategoryRepository
import com.example.spektar.models.repositories.GameRepository
import com.example.spektar.models.repositories.MovieRepository
import com.example.spektar.models.repositories.ShowRepository
import com.example.spektar.screens.mediaCategories.MediaViewModel
import com.example.spektar.screens.mediaCategories.MediaViewModelFactory
import com.example.spektar.screens.userScreens.SignInViewModel
import com.example.spektar.screens.userScreens.SignInViewModelFactory
import com.example.spektar.screens.userScreens.SignUpViewModel
import com.example.spektar.screens.userScreens.SignUpViewModelFactory
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

object FirestoreManager {
    val db: FirebaseFirestore by lazy { Firebase.firestore }
}

/*
MainActivity is the place where my firebase DB is defined, alongside where all ViewModels are defined.
The ViewModels get constructed and then are passed into Navigation.kt, where they are used depending on the screen.
 */

/*
TODO:
    - make a color palette cause material3 highk sucks?
     make every imageRow in CategoryPageScreen be a different color

    - figure out user content recommendation,
     which also means i need to implement tags for the media
     and also implement search

    - sort out error handling (on data requests pretty much, make error screens and components for it)

    - implement a few screens, most importantly a user profile screen which means that i also need to
    implement multiple-languages support, photosensitive mode...
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpektarTheme(
                dynamicColor = false
            ) {
                val mediaViewModel: MediaViewModel by viewModels {
                    MediaViewModelFactory(
                        CategoryRepository(),

                        ShowRepository(FirebaseFirestore.getInstance()),
                        BookRepository(FirebaseFirestore.getInstance()),
                        GameRepository(FirebaseFirestore.getInstance()),
                        MovieRepository(db = FirebaseFirestore.getInstance())
                    )
                }

                val signInViewModel: SignInViewModel by viewModels {
                    SignInViewModelFactory(
                        accountService = AccountServiceImpl()
                    )
                }

                val signUpViewModel: SignUpViewModel by viewModels {
                    SignUpViewModelFactory(
                        accountService = AccountServiceImpl()
                    )
                }

                SpektarNavigation(
                    mediaViewModel,
                    signInViewModel,
                    signUpViewModel,
                )
            }
        }
    }
}