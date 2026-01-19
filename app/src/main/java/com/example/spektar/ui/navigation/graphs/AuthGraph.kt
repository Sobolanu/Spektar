package com.example.spektar.ui.navigation.graphs

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.test.internal.platform.app.`ActivityInvoker$$CC`
import com.example.spektar.ui.viewModels.states.UserSignInData
import com.example.spektar.domain.usecase.AccountServiceImpl
import com.example.spektar.ui.navigation.routes.CategoryScreen
import com.example.spektar.ui.navigation.routes.UserLoginScreen
import com.example.spektar.ui.navigation.routes.UserRegistrationScreen
import com.example.spektar.ui.navigation.utils.navTypeOf
import com.example.spektar.ui.navigation.utils.safeNavigate
import com.example.spektar.ui.userLoginScreens.UserLoginScreen
import com.example.spektar.ui.userLoginScreens.UserRegistrationScreen
import com.example.spektar.ui.viewModels.SignInViewModel
import com.example.spektar.ui.viewModels.SignInViewModelFactory
import com.example.spektar.ui.viewModels.SignUpViewModel
import com.example.spektar.ui.viewModels.SignUpViewModelFactory
import kotlin.reflect.typeOf

fun NavGraphBuilder.AuthGraph(
    navController: NavController,
    // signInViewModel: SignInViewModel,
    // signUpViewModel: SignUpViewModel,
) {
    composable<UserLoginScreen>(
        typeMap = mapOf(typeOf<UserLoginScreen>() to navTypeOf<UserLoginScreen>())
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<UserLoginScreen>()

        val signInViewModel: SignInViewModel = viewModel(
            factory = SignInViewModelFactory(accountService = AccountServiceImpl())
        )
        val uiState = signInViewModel.userSignInData.collectAsState()

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
        val uiState = signUpViewModel.userSignUpData.collectAsState()

        UserRegistrationScreen(
            state = uiState.value,
            onEvent = { authEvent ->
                signUpViewModel.onEvent(authEvent)
            },
            onSignUp = { navController.safeNavigate(UserLoginScreen(true) ) } // read comment above
        )
    }
}