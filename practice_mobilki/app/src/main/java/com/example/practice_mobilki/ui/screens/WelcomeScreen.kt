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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.components.BlockButton
import com.example.practice_mobilki.ui.theme.CustomColors
import com.example.practice_mobilki.ui.theme.TypographyApplication
import kotlin.to

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier, onNext: () -> Unit) {
    val gradient = Brush.linearGradient(0.0f to CustomColors.accent, 500.0f to CustomColors.disable, start = Offset.Zero, end = Offset.Infinite)
    Column(
        modifier = Modifier.fillMaxSize().background(gradient),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = stringResource(R.string.welcome),
            style = TypographyApplication.headingBold30,
            color = CustomColors.block
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.fillMaxWidth().weight(3f)) {
            Image(
                painter = painterResource(R.drawable.foot_image),
                contentDescription = "foot",
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.pager1),
            contentDescription = "page 1"
        )
        Spacer(modifier = Modifier.weight(1f))
        BlockButton(
            onClick = {
                onNext()
            },
            text = stringResource(R.string.start),
            modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.weight(0.5f))

    }
}

@Preview
@Composable
private fun WelcomeScreenPreview() {
    WelcomeScreen(onNext = {})
}