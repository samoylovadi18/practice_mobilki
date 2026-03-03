package com.example.practice_mobilki.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.theme.CustomColors

@Composable
fun SuccessDialog(
    show: Boolean,
    onDismiss: () -> Unit
) {
    println("SuccessDialog - show: $show")

    LaunchedEffect(show) {
        println("SuccessDialog LaunchedEffect - show changed to: $show")
    }

    if (show) {
        println("SuccessDialog - Rendering dialog")

        Dialog(onDismissRequest = {
            println("SuccessDialog - onDismissRequest called")
            onDismiss()
        }) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    // Синий круг с иконкой email поверх
                    Box(
                        modifier = Modifier.size(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Синий круг из ресурса shape.xml
                        Image(
                            painter = painterResource(id = R.drawable.shape),
                            contentDescription = "Blue Circle Background",
                            modifier = Modifier.size(80.dp)
                        )

                        // Иконка email поверх круга из email.xml
                        Image(
                            painter = painterResource(id = R.drawable.email),
                            contentDescription = "Email Icon",
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Заголовок
                    Text(
                        text = "Check Your Email",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Текст сообщения
                    Text(
                        text = "We have sent a password recovery\ncode to your email address.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SuccessDialogPreview() {
    val showDialog = remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = 0.3f))
    ) {
        SuccessDialog(
            show = showDialog.value,
            onDismiss = { showDialog.value = false }
        )
    }
}