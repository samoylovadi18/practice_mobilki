package com.example.practice_mobilki.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practice_mobilki.ui.components.CustomAlertDialog
import com.example.practice_mobilki.ui.screens.ForgotPasswordScreen
import com.example.practice_mobilki.ui.screens.OTPVerificationScreen
import com.example.practice_mobilki.ui.screens.SignInScreen
import com.example.practice_mobilki.ui.screens.SignUpScreen
import com.example.practice_mobilki.ui.viewmodel.SignInViewModel
import com.example.practice_mobilki.ui.viewmodel.SignUpViewModel

// Класс для хранения маршрутов
sealed class Screen(val route: String) {
    data object SignUp : Screen("sign_up")
    data object SignIn : Screen("sign_in")
    data object ForgotPassword : Screen("forgot_password")
    data object OTPVerification : Screen("otp_verification")
    data object Home : Screen("home")
    data object Profile : Screen("profile")
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.SignIn.route,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        contentAlignment = contentAlignment,
        enterTransition = { fadeIn(animationSpec = tween(700)) },
        exitTransition = { fadeOut(animationSpec = tween(700)) },
        popEnterTransition = { fadeIn(animationSpec = tween(700)) },
        popExitTransition = { fadeOut(animationSpec = tween(700)) }
    ) {
        // ЭКРАН РЕГИСТРАЦИИ
        composable(route = Screen.SignUp.route) {
            val viewModel: SignUpViewModel = viewModel()
            val isSignUpSuccessful by viewModel.isSignUpSuccessful

            LaunchedEffect(isSignUpSuccessful) {
                if (isSignUpSuccessful) {
                    viewModel.resetSignUpState()
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
            }

            SignUpScreen(
                viewModel = viewModel,
                onSignUpSuccess = { },
                toSignInScreen = {
                    navController.navigate(Screen.SignIn.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )

            if (viewModel.showDialog.value) {
                CustomAlertDialog(
                    show = viewModel.showDialog.value,
                    onDismiss = { viewModel.hideDialog() },
                    text = viewModel.dialogText.value,
                    title = viewModel.dialogTitle.value
                )
            }
        }

        // ЭКРАН ВХОДА
        composable(route = Screen.SignIn.route) {
            val viewModel: SignInViewModel = viewModel()
            val isSignInSuccessful by viewModel.isSignInSuccessful

            LaunchedEffect(isSignInSuccessful) {
                if (isSignInSuccessful) {
                    viewModel.resetSignInState()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                }
            }

            SignInScreen(
                viewModel = viewModel,
                onSignInSuccess = { },
                toSignUpScreen = {
                    navController.navigate(Screen.SignUp.route)
                },
                toForgotPasswordScreen = {
                    navController.navigate(Screen.ForgotPassword.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )

            if (viewModel.showDialog.value) {
                CustomAlertDialog(
                    show = viewModel.showDialog.value,
                    onDismiss = { viewModel.hideDialog() },
                    text = viewModel.dialogText.value,
                    title = viewModel.dialogTitle.value
                )
            }
        }

        // ЭКРАН ВОССТАНОВЛЕНИЯ ПАРОЛЯ
        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToOTP = {
                    navController.navigate(Screen.OTPVerification.route)
                }
            )
        }

        // ЭКРАН OTP VERIFICATION
        composable(route = Screen.OTPVerification.route) {
            OTPVerificationScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onVerifyClick = { code ->
                    println("Verifying code: $code")
                }
            )
        }

        // ГЛАВНЫЙ ЭКРАН
        composable(route = Screen.Home.route) {
            HomePlaceholder(
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        // ЭКРАН ПРОФИЛЯ
        composable(route = Screen.Profile.route) {
            ProfilePlaceholder(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun OTPVerificationScreen(onBackClick: () -> Boolean, onVerifyClick: (ERROR) -> Unit) {
    TODO("Not yet implemented")
}

annotation class ERROR

// ЗАГЛУШКА ДЛЯ HOME
@Composable
fun HomePlaceholder(onProfileClick: () -> Unit) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Home Screen",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onProfileClick,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Перейти в профиль")
            }
        }
    }
}

// ЗАГЛУШКА ДЛЯ PROFILE
@Composable
fun ProfilePlaceholder(onBack: () -> Unit) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Profile Screen",
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onBack,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Назад")
            }
        }
    }
}