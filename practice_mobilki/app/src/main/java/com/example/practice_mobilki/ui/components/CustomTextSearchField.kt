package com.example.practice_mobilki.ui.components

import android.widget.NumberPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.theme.CustomColors

@Composable
fun CustomTextSearchField(modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit, placeholderText: String) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.marker_1),
                contentDescription = "An eye",
                modifier = Modifier.padding(paddingValues = PaddingValues(start = 15.dp))
            )
        },
        colors = TextFieldDefaults
            .colors(
                unfocusedContainerColor = CustomColors.background,
                focusedContainerColor = CustomColors.background,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        placeholder = { Text(text = placeholderText, color = CustomColors.hint) }
    )
}

@Preview
@Composable
private fun CustomTextFieldPreview() {
    CustomTextSearchField(value = "", onValueChange = {}, placeholderText = "Поиск")
}