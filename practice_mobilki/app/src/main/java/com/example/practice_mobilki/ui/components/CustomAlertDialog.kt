package com.example.practice_mobilki.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.practice_mobilki.ui.theme.TypographyApplication

@Composable
fun CustomAlertDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onAccept: () -> Unit = onDismiss,
    acceptText: String = "ОК",
    title: String,
    text: String
    ) {
        if (show) {
            AlertDialog(
                onDismissRequest = onDismiss,
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(title, style = TypographyApplication.headingSemiBold16)
                    }
                },
                text = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(text, style = TypographyApplication.bodyRegular16)
                    }
                },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        SubTextButton(
                            modifier = Modifier.weight(2f),
                            onClick = onDismiss,
                            text = "Отмена"
                        )
                        Spacer(modifier = Modifier.weight(0.25f))
                        AccentButton(
                            modifier = Modifier.weight(2f),
                            onClick = onAccept,
                            text = acceptText
                        )
                    }
                }
            )
        }
}

@Composable
fun SubTextButton(modifier: Modifier, onClick: () -> Unit, text: String) {
    TODO("Not yet implemented")
}

@Preview
@Composable
private fun CustomAlertDialogPreview() {
    CustomAlertDialog(true, {}, title = "Внимание", text = "dexgd[sfgsa")
}