package com.example.spektar.ui.navigation.graphs.authGraph

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.spektar.data.remote.AccountServiceImpl
import com.example.spektar.ui.navigation.graphs.categoryGraph.CategoryScreen
import com.example.spektar.ui.navigation.utils.navTypeOf
import com.example.spektar.ui.navigation.utils.safeNavigate
import com.example.spektar.ui.userAuthScreens.loginScreen.SignInViewModel
import com.example.spektar.ui.userAuthScreens.loginScreen.SignInViewModelFactory
import com.example.spektar.ui.userAuthScreens.loginScreen.UserLoginScreen
import com.example.spektar.ui.userAuthScreens.signUpScreen.SignUpViewModel
import com.example.spektar.ui.userAuthScreens.signUpScreen.SignUpViewModelFactory
import com.example.spektar.ui.userAuthScreens.signUpScreen.UserRegistrationScreen
import kotlin.reflect.typeOf

fun NavGraphBuilder.AuthGraph(
    navController: NavController,
) {
    composable<UserLoginScreen>(
        typeMap = mapOf(typeOf<UserLoginScreen>() to navTypeOf<UserLoginScreen>())
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<UserLoginScreen>()

        val signInViewModel: SignInViewModel = viewModel(
            factory = SignInViewModelFactory(accountService = AccountServiceImpl())
        )
        val uiState = signInViewModel.signInRequest.collectAsState()

        UserLoginScreen(
            onSignInClick = { navController.safeNavigate(CategoryScreen) }, // placeholder route until i make home screen
            onTextClick = { navController.safeNavigate(UserRegistrationScreen) },
            state = uiState.value,
            onEvent = { authEvent ->
                signInViewModel.onEvent(authEvent)
            },
            showEmailPopUp = args.showEmailPopUp,
        )
    }

    composable<UserRegistrationScreen> {
        val signUpViewModel: SignUpViewModel = viewModel(
            factory = SignUpViewModelFactory(accountService = AccountServiceImpl())
        )
        val uiState = signUpViewModel.signUpRequest.collectAsState()

        UserRegistrationScreen(
            state = uiState.value,
            onEvent = { authEvent ->
                signUpViewModel.onEvent(authEvent)
            },
            onSignUp = { navController.safeNavigate(UserLoginScreen(true)) } // read comment above
        )
    }
}