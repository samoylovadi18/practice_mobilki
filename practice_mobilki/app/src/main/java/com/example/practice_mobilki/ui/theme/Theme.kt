package com.example.practice_mobilki.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun ShoeStoreTheme(
    content: @Composable () -> Unit
) {
    val customColors = CustomColors

    MaterialTheme(
    ) {
        ProvideCustomColors(customColors = customColors) {
            content()
        }
    }
}
object TypographyApplication {
    val headingRegular34 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        lineHeight = 40.sp
    )
    val headingRegular32 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 38.sp
    )
    val headingBold30 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 36.sp
    )
    val headingRegular26 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp,
        lineHeight = 32.sp
    )
    val headingRegular20 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp
    )
    val headingSemiBold16 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp
    )
    val subtitleRegular16 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp
    )
    val bodyRegular24 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 28.sp
    )
    val bodyRegular20 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 24.sp
    )
    val bodySemiBold18 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    )
    val bodyMedium16 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp
    )
    val bodyRegular16 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp
    )
    val bodyMedium14 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )
    val bodyRegular14 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )
    val bodyRegular12 = TextStyle(
        fontFamily = ralewayRegular,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    )
}
