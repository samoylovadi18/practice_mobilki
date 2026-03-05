package com.example.practice_mobilki.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice_mobilki.ui.theme.CustomColors

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    trailingIcon: @Composable (() -> Unit)? = null,
    isEnabled: Boolean = true,
    isError: Boolean = false
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        shape = RoundedCornerShape(14.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = CustomColors.background,
            focusedContainerColor = CustomColors.background,
            focusedIndicatorColor = if (isError) Color.Red else Color.Transparent,
            unfocusedIndicatorColor = if (isError) Color.Red else Color.Transparent,
            disabledContainerColor = CustomColors.subTextLight,
            errorContainerColor = CustomColors.background.copy(alpha = 0.5f),
            errorIndicatorColor = Color.Red,
            errorLabelColor = Color.Red,
            errorTrailingIconColor = Color.Red
        ),
        placeholder = {
            Text(
                text = placeholderText,
                color = if (isError) Color.Red else CustomColors.hint
            )
        },
        trailingIcon = trailingIcon,
        enabled = isEnabled,
        isError = isError
    )
}

@Preview
@Composable
private fun CustomTextFieldPreview() {
    CustomTextField(
        value = "",
        onValueChange = {},
        placeholderText = "xxxxxxxx",
        isEnabled = false
    )
}