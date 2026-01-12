package com.example.spektar.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.spektar.ui.navigation.navTypeUtils.navTypeOf
import com.example.spektar.ui.navigation.routes.CategoryScreen
import com.example.spektar.ui.navigation.routes.UserLoginScreen
import com.example.spektar.ui.navigation.routes.UserRegistrationScreen
import com.example.spektar.ui.userLoginScreens.UserLoginScreen
import com.example.spektar.ui.userLoginScreens.UserRegistrationScreen
import com.example.spektar.ui.viewModels.SignInViewModel
import com.example.spektar.ui.viewModels.SignUpViewModel
import kotlin.reflect.typeOf

fun NavGraphBuilder.AuthGraph(
    navController: NavController,
    signInViewModel: SignInViewModel,
    signUpViewModel: SignUpViewModel
) {
    composable<UserLoginScreen>(
        typeMap = mapOf(typeOf<UserLoginScreen>() to navTypeOf<UserLoginScreen>())
    ) { backStackEntry ->
        val args = backStackEntry.toRoute<UserLoginScreen>()

        UserLoginScreen(
            onSignInClick = { navController.navigate(CategoryScreen) { launchSingleTop = true } }, // placeholder route until i make home screen
            onTextClick = { navController.navigate(UserRegistrationScreen) { launchSingleTop = true } },
            viewModel = signInViewModel,
            showEmailPopUp = args.showEmailPopUp,
            onDismiss = { navController.navigate(UserLoginScreen(false)) }
        )
    }

    composable<UserRegistrationScreen> {
        UserRegistrationScreen(
            viewModel = signUpViewModel,
            onSignUp = { navController.navigate(UserLoginScreen(true) ) { launchSingleTop = true } } // read comment above
        )
    }
}