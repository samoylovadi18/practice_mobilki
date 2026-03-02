package com.example.practice_mobilki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.practice_mobilki.navigation.AppNavHost
import com.example.practice_mobilki.navigation.Screen
import com.example.practice_mobilki.ui.theme.ShoeStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoeStoreTheme {
                // Создаем контроллер навигации
                val navController = rememberNavController()

                // Используем наш NavHost
                AppNavHost(
                    navController = navController,
                    startDestination = Screen.SignUp.route,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppNavHost() {
    ShoeStoreTheme {
        AppNavHost()
    }
}