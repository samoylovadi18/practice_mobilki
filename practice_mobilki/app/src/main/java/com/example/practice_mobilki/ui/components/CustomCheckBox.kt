// CustomCheckbox.kt
package com.example.practice_mobilki.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.theme.CustomColors

@Composable
fun CustomCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    uncheckedColor: Color = CustomColors.background,
    checkedColor: Color = CustomColors.accent
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .background(
                color = if (checked) checkedColor else uncheckedColor,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.policy_check),
            contentDescription = null,
            modifier = Modifier.size(13.dp),
            alpha = 1f
        )

        AnimatedVisibility(
            visible = checked,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Image(
                painter = painterResource(R.drawable.policy_check),
                contentDescription = "Checked",
                modifier = Modifier.size(13.dp)
            )
        }
    }
}
