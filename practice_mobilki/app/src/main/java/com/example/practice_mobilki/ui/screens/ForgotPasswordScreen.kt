package com.example.practice_mobilki.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.components.AccentButton
import com.example.practice_mobilki.ui.components.CustomTextField
import com.example.practice_mobilki.ui.components.SuccessDialog
import com.example.practice_mobilki.ui.theme.CustomColors

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    onSendClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Функция валидации email (упрощенная для теста)
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && email.contains("@") && email.contains(".")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Кнопка "Назад"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = CustomColors.background,
                        shape = RoundedCornerShape(50)
                    )
                    .clickable { onBackClick() },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.back),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    alpha = 1f
                )
            }
        }

        // Контент
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Заголовок
            Text(
                text = "Forgot Password",
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Подзаголовок
            Text(
                text = "Enter your account email\nto reset your password",
                fontSize = 16.sp,
                color = CustomColors.hint,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Поле ввода email
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                placeholderText = "xyz@gmail.com"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Кнопка "Send"
            AccentButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                text = "Send",
                onClick = {
                    if (isValidEmail(email)) {
                        showSuccessDialog = true
                        onSendClick(email)
                    } else {
                        // Добавим toast или уведомление для отладки
                        println("Invalid email: $email")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    // Диалог успешной отправки
    SuccessDialog(
        show = showSuccessDialog,
        onDismiss = { showSuccessDialog = false }
    )
}

@Preview(showBackground = true)
@Composable
private fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        onBackClick = {},
        onSendClick = {}
    )
}