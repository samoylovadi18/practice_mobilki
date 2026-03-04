package com.example.practice_mobilki.ui.components


import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice_mobilki.ui.theme.CustomColors
import com.example.practice_mobilki.ui.theme.TypographyApplication

@Composable
fun BlockButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit, enabled: Boolean = true) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CustomColors.block,
            disabledContainerColor = CustomColors.disable,
            disabledContentColor = CustomColors.block
        ),
        enabled = enabled
    ) {
        Text(text = text, color = CustomColors.text, style = TypographyApplication.bodyRegular14)
    }
}

@Preview
@Composable
private fun AccentButtonPreview() {
    BlockButton(text = "some Text", onClick = {})
}