package com.example.spektar.ui.navigation.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.spektar.ui.navigation.routes.CategoryScreen
import com.example.spektar.ui.navigation.routes.UserLoginScreen
import com.example.spektar.ui.navigation.routes.UserRegistrationScreen
import com.example.spektar.ui.userScreens.CreateUserScreen
import com.example.spektar.ui.userScreens.UserRegistrationScreen
import com.example.spektar.ui.viewModels.SignInViewModel
import com.example.spektar.ui.viewModels.SignUpViewModel

fun NavGraphBuilder.AuthGraph(
    navController: NavController,
    signInViewModel: SignInViewModel,
    signUpViewModel: SignUpViewModel
) {
    composable<UserLoginScreen> {
        CreateUserScreen(
            onSignInClick = { navController.navigate(CategoryScreen) {launchSingleTop = true} }, // placeholder route until i make home screen
            onTextClick = { navController.navigate(UserRegistrationScreen) {launchSingleTop = true} },
            viewModel = signInViewModel
        )
    }

    composable<UserRegistrationScreen> {
        UserRegistrationScreen(
            viewModel = signUpViewModel,
            onSignUp = { navController.navigate(CategoryScreen ) {launchSingleTop = true} } // read comment above
        )
    }
}