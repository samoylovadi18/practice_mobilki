package com.example.practice_mobilki.ui.components

import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice_mobilki.ui.theme.CustomColors

@Composable
fun CustomTextField(modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit, placeholderText: String, trailingIcon : @Composable (() -> Unit)? = null, isEnabled: Boolean = true) {
    TextField(modifier = modifier,
              value = value,
              onValueChange = onValueChange,
              shape = RoundedCornerShape(14.dp),
              colors = TextFieldDefaults
                    .colors(
                        unfocusedContainerColor = CustomColors.background,
                        focusedContainerColor = CustomColors.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledContainerColor = CustomColors.subTextLight),
        placeholder = { Text(text = placeholderText, color = CustomColors.hint) },
        trailingIcon = trailingIcon,
        enabled = isEnabled)
}

@Preview
@Composable
private fun CustomTextFieldPreview() {
    CustomTextField(value = "", onValueChange = {}, placeholderText = "xxxxxxxx", isEnabled = false)
}