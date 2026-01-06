@file:Suppress("unused")

package com.example.spektar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.room.Room
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
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

val supabase = createSupabaseClient(
    supabaseUrl = "https://rlyotyktmhyflfyljpmr.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJseW90eWt0bWh5ZmxmeWxqcG1yIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjY0MTM3MDgsImV4cCI6MjA4MTk4OTcwOH0.eTwSGVag_npnTkiQtOCqBLxSidqMAM2psJEAN7h4TEQ"
) {
    install(Postgrest)
}
// next: migrate user auth from firebase to supabase

/*
MainActivity is the place where my firebase DB is defined, alongside where all ViewModels are defined.
The ViewModels get constructed and then are passed into Navigation.kt, where they are used depending on the screen.
 */

/* TODO:
    ### FRONT END
        - home screen
            - this home screen would contain pieces of media you're currently consuming, and they'd
            have a progress counter (page X/Y for books and an optional daily goal for books, whereas
            for shows, each season gets it's own "slot" in the repositories, so it'll just be of format
            "Episode X/Y", where X means the last page/episode you watched, Y is max)

        - screen for each media's note
            - peep the bottom of this comment for elaborations (the implement per-media part)

        - profile screen
            - might be redundant if you can integrate into settings screen

        - questionnaire screen

        - settings screen
            - photosensitive mode, profile settings, app language, dynamic color on/off and other
            important stuff you think of

        - splash screen
            - app logo somehow slowly gaining color

        - grid screen for "More..." at the end of the image rows

        - make a color palette cause material3 highk sucks?
            - Primary color is js gonna be some red, reminder that primary color is just "what color
            do you think of when you think of this app". it serves zero purpose other than that
            - secondary and tertiary you have written yourself in CategoryPageScreen.kt where the top
            and bottom bars respectively are.
            - your Surface should be the background color of CategoryPageScreen.kt, whereas your
            SurfaceVariant (or whtv it's called in the builder) should be that one shade of red you
            used before you had a different background color for each category

    TODO: ### BACK-END (sort out after designing screen UIs else you'll just get mad and burn out)
        - after splash screen designing, have the app auto log-on if you have made an account

         - figure out user content recommendation,
         which also means i need to implement tags for the media and also implement search
            - so basically, the shtick for user content recommendation is that you'll make an account,
            then fill out a questionnaire a la old netflix, to deduce what you'll want to consume next
            recommended media would be among the first images in image rows

        - sort out error handling
            - handle any potential errors from firebase realtime database and user auth

        - implement per-media notes properly
            - Each media should have a LIST of notes belonging to it
            - each note should have a custom user-assigned title, and content that is modifiable
            the content should have modifiable text size, text color, bold/underline/italic,
            hyperlinks that lead to other notes and URLs and stuff y'know
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

                        ShowRepository(),
                        BookRepository(),
                        GameRepository(),
                        MovieRepository()
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