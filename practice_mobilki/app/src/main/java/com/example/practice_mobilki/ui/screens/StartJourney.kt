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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.components.BlockButton
import com.example.practice_mobilki.ui.theme.CustomColors
import com.example.practice_mobilki.ui.theme.TypographyApplication
import com.example.practice_mobilki.ui.theme.ralewayRegular
import kotlin.to

@Composable
fun StartJourney(modifier: Modifier = Modifier, onNext: () -> Unit) {
    val gradient = Brush.linearGradient(0.0f to CustomColors.accent, 500.0f to CustomColors.disable, start = Offset.Zero, end = Offset.Infinite)
    Column(
        modifier = Modifier.fillMaxSize().background(gradient),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier = Modifier.fillMaxWidth().weight(2f)) {
            Image(
                painter = painterResource(R.drawable.sneakers_image1),
                contentDescription = "sneakers",
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.start_journey),
            style = TypographyApplication.headingRegular34,
            color = CustomColors.block
        )
        Spacer(modifier = Modifier.height(13.dp))
        Text(
            text = stringResource(R.string.cool_collection),
            style = TypographyApplication.bodyRegular16,
            color = CustomColors.subTextLight,
            modifier = Modifier.width(315.dp),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(R.drawable.pager2),
            contentDescription = "page 2"
        )
        Spacer(modifier = Modifier.weight(1f))
        BlockButton(
            onClick = {
                onNext()
            },
            text = stringResource(R.string.next),
            modifier = Modifier.fillMaxWidth().height(50.dp).padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.weight(0.25f))
    }
}

@Preview
@Composable
private fun StartJourneyPreview() {
    StartJourney(onNext = {})
}