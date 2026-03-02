package com.example.practice_mobilki.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

object CustomColors {
    val primary: Color = Color(0xFF6750A4)
    val red = Color(0xFFF87265)
    val accent = Color(0xFF48B2E7)
    val disable = Color(0xFF2B6B8B)
    val subTextLight = Color(0xFFD8D8D8)
    val background = Color(0xFFF7F7F9)
    val block = Color(0xFFFFFFFF)
    val text = Color(0xFF2B2B2B)
    val hint = Color(0xFF6A6A6A)
    val subTextDark = Color(0xFF707B81)
}
@Composable
fun ProvideCustomColors(customColors: com.example.practice_mobilki.ui.theme.CustomColors, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalCustomColors provides customColors, content = content)
}
val LocalCustomColors = compositionLocalOf { com.example.practice_mobilki.ui.theme.CustomColors }