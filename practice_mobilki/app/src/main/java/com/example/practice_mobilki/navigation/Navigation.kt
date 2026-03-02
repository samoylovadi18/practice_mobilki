package com.example.practice_mobilki.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practice_mobilki.ui.components.CustomAlertDialog
import com.example.practice_mobilki.ui.screens.SignUpScreen
import com.example.practice_mobilki.ui.viewmodel.SignUpViewModel

// Класс для хранения маршрутов
sealed class Screen(val route: String) {
    data object SignUp : Screen("sign_up")
    data object SignIn : Screen("sign_in")
    data object Home : Screen("home")
    data object Profile : Screen("profile")
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.SignUp.route,
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
        // Экран регистрации
        composable(route = Screen.SignUp.route) {
            val viewModel: SignUpViewModel = viewModel()
            val isSignUpSuccessful by viewModel.isSignUpSuccessful

            // Следим за успешной регистрацией
            LaunchedEffect(isSignUpSuccessful) {
                if (isSignUpSuccessful) {
                    // Сбрасываем состояние перед навигацией
                    viewModel.resetSignUpState()
                    // Переходим на главный экран
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                }
            }

            SignUpScreen(
                viewModel = viewModel,
                onSignUpSuccess = {
                    // Этот колбэк вызывается из ViewModel при успешной регистрации
                    // Но мы уже обрабатываем это через LaunchedEffect выше
                },
                toSignInScreen = {
                    navController.navigate(Screen.SignIn.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )

            // Диалог с ошибкой
            if (viewModel.showDialog.value) {
                CustomAlertDialog(
                    show = viewModel.showDialog.value,
                    onDismiss = { viewModel.hideDialog() },
                    text = viewModel.dialogText.value,
                    title = viewModel.dialogTitle.value
                )
            }
        }

        // Экран входа
        composable(route = Screen.SignIn.route) {
            SignInPlaceholder(
                onSignInSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.SignIn.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // Главный экран
        composable(route = Screen.Home.route) {
            HomePlaceholder(
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        // Экран профиля
        composable(route = Screen.Profile.route) {
            ProfilePlaceholder(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

// Заглушки для других экранов
@Composable
fun SignInPlaceholder(
    onSignInSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Sign In Screen")
            Button(
                onClick = onSignInSuccess,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Войти")
            }
            Button(
                onClick = onSignUpClick,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Перейти на регистрацию")
            }
            Button(
                onClick = onBack,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Назад")
            }
        }
    }
}

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
            Text("Home Screen")
            Button(
                onClick = onProfileClick,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Перейти в профиль")
            }
        }
    }
}

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
            Text("Profile Screen")
            Button(
                onClick = onBack,
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Назад")
            }
        }
    }
}