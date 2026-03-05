package com.example.practice_mobilki.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.practice_mobilki.ui.components.CustomAlertDialog
import com.example.practice_mobilki.ui.screens.DetailsScreen
import com.example.practice_mobilki.ui.screens.FavoriteScreen
import com.example.practice_mobilki.ui.screens.ForgotPasswordScreen
import com.example.practice_mobilki.ui.screens.Home
import com.example.practice_mobilki.ui.screens.OnboardingScreen
import com.example.practice_mobilki.ui.screens.OtpVerificationScreen
import com.example.practice_mobilki.ui.screens.OutdoorCategoryScreen
import com.example.practice_mobilki.ui.screens.ProfileScreen
import com.example.practice_mobilki.ui.screens.SignInScreen
import com.example.practice_mobilki.ui.screens.SignUpScreen
import com.example.practice_mobilki.ui.viewmodel.ProductsViewModel
import com.example.practice_mobilki.ui.viewmodel.ProfileViewModel
import com.example.practice_mobilki.ui.viewmodel.SignInViewModel
import com.example.practice_mobilki.ui.viewmodel.SignUpViewModel
import com.example.practice_mobilki.ui.viewmodel.VerifyOTPViewModel

// Класс для хранения маршрутов

//
sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object SignUp : Screen("sign_up")
    data object SignIn : Screen("sign_in")
    data object ForgotPassword : Screen("forgot_password")
    data object OTPVerification : Screen("otp_verification")
    data object Home : Screen("home")
    data object Profile : Screen("profile")
    data object Favorite : Screen("favorite")
    data object Cart : Screen("cart")
    data object Notification : Screen("notification")
    data object OutdoorCategory : Screen("outdoor_category")
    data object Details : Screen("details")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

    fun detailsWithId(productId: String): String = "details/$productId"
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.SignIn.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { fadeIn(animationSpec = tween(700)) },
        exitTransition = { fadeOut(animationSpec = tween(700)) },
        popEnterTransition = { fadeIn(animationSpec = tween(700)) },
        popExitTransition = { fadeOut(animationSpec = tween(700)) }
    ) {
        // ЭКРАН ВХОДА
        composable(route = Screen.SignIn.route) {
            val viewModel: SignInViewModel = viewModel()
            val isSignInSuccessful by viewModel.isSignInSuccessful

            LaunchedEffect(isSignInSuccessful) {
                if (isSignInSuccessful) {
                    viewModel.resetSignInState()
                    navController.navigate(Screen.Onboarding.route) {
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

        // ЭКРАН ОНБОРДИНГА
        composable(route = Screen.Onboarding.route) {
            OnboardingScreen(
                onOnboardingComplete = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // ЭКРАН РЕГИСТРАЦИИ
        composable(route = Screen.SignUp.route) {
            val viewModel: SignUpViewModel = viewModel()
            val isSignUpSuccessful by viewModel.isSignUpSuccessful
            val userEmail by viewModel.userEmail

            LaunchedEffect(isSignUpSuccessful) {
                if (isSignUpSuccessful) {
                    viewModel.resetSignUpState()
                    navController.navigate(Screen.OTPVerification.withArgs(userEmail)) {
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

        // ЭКРАН ВОССТАНОВЛЕНИЯ ПАРОЛЯ
        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToOTP = { email ->
                    navController.navigate(Screen.OTPVerification.withArgs(email))
                }
            )
        }

        // ЭКРАН OTP VERIFICATION
        composable(
            route = "${Screen.OTPVerification.route}/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val viewModel: VerifyOTPViewModel = viewModel()

            OtpVerificationScreen(
                modifier = Modifier,
                viewModel = viewModel,
                onVerifyOTPSuccess = {
                    navController.navigate(Screen.SignIn.route) {
                        popUpTo(Screen.OTPVerification.route) { inclusive = true }
                    }
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ГЛАВНЫЙ ЭКРАН
        composable(route = Screen.Home.route) {
            val productsViewModel: ProductsViewModel = viewModel()

            Home(
                onProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onFavorite = {
                    navController.navigate(Screen.Favorite.route)
                },
                onNotification = {
                    navController.navigate(Screen.Notification.route)
                },
                onCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onNavigateToOutdoor = {
                    navController.navigate(Screen.OutdoorCategory.route)
                },
                onProductClick = { productId ->
                    navController.navigate(Screen.Details.detailsWithId(productId))
                },
                viewModel = productsViewModel
            )
        }

        // ЭКРАН OUTDOOR КАТЕГОРИИ
        composable(route = Screen.OutdoorCategory.route) {
            OutdoorCategoryScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onProductClick = { productId ->
                    navController.navigate(Screen.Details.detailsWithId(productId))
                }
            )
        }

        // ЭКРАН ДЕТАЛЕЙ ТОВАРА
        composable(
            route = "details/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val productsViewModel: ProductsViewModel = viewModel()

            val product = productsViewModel.uiState.value.products.find { it.id == productId }

            if (product != null) {
                DetailsScreen(
                    product = product,
                    onBackClick = { navController.popBackStack() },
                    onAddToCart = {
                        productsViewModel.addToCart(productId)
                    },
                    onToggleFavorite = {
                        productsViewModel.toggleFavorite(productId)
                    }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Товар не найден")
                }
            }
        }

        // ЭКРАН ПРОФИЛЯ
        composable(route = Screen.Profile.route) {
            val profileViewModel: ProfileViewModel = viewModel()

            ProfileScreen(
                onProfile = { },
                onHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                },
                onFavorite = {
                    navController.navigate(Screen.Favorite.route)
                },
                onNotification = {
                    navController.navigate(Screen.Notification.route)
                },
                onCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onOpenCamera = {
                    profileViewModel.onOpenCamera()
                },
                viewModel = profileViewModel
            )
        }

        // ЭКРАН ИЗБРАННОГО
        composable(route = Screen.Favorite.route) {
            FavoriteScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onProductClick = { productId ->
                    navController.navigate(Screen.Details.detailsWithId(productId))
                },
                onRemoveFromFavorite = { productId ->
                    // Логика удаления
                }
            )
        }

        // ЭКРАН КОРЗИНЫ (заглушка)
        composable(route = Screen.Cart.route) {
            PlaceholderScreen(
                title = "Корзина",
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ЭКРАН УВЕДОМЛЕНИЙ (заглушка)
        composable(route = Screen.Notification.route) {
            PlaceholderScreen(
                title = "Уведомления",
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

// Универсальная заглушка
@Composable
fun PlaceholderScreen(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "$title (В разработке)",
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