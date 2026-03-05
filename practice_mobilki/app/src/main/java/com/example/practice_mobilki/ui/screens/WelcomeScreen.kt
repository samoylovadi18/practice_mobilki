package com.example.practice_mobilki.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.components.BlockButton
import com.example.practice_mobilki.ui.theme.CustomColors
import com.example.practice_mobilki.ui.theme.TypographyApplication

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit
) {
    val gradient = Brush.linearGradient(
        0.0f to CustomColors.accent,
        500.0f to CustomColors.disable,
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(gradient)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Текст наверху
        Text(
            text = "ДОБРО \n" +
                    "ПОЖАЛОВАТЬ",
            style = TypographyApplication.headingBold30,
            color = CustomColors.block,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        // Изображение
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            Image(
                painter = painterResource(R.drawable.foot_image),
                contentDescription = "foot",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // Индикатор страницы
        Image(
            painter = painterResource(R.drawable.pager1),
            contentDescription = "page 1",
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Кнопка
        BlockButton(
            onClick = onNext,
            text = "Начать",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    WelcomeScreen(onNext = {})
}