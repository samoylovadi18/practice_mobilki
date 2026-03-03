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
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.delay
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.components.CustomTextField
import com.example.practice_mobilki.ui.theme.CustomColors

@Composable
fun OTPVerificationScreen(
    onBackClick: () -> Unit,
    onVerifyClick: (String) -> Unit,
    onResendClick: () -> Unit = { println("Resend code") },
    modifier: Modifier = Modifier
) {
    var otpCode by remember { mutableStateOf("") }
    var timer by remember { mutableStateOf(60) }
    var isTimerActive by remember { mutableStateOf(true) }
    var canResend by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) } // Для отслеживания ошибки
    var errorMessage by remember { mutableStateOf("") }

    // Таймер обратного отсчета
    LaunchedEffect(isTimerActive) {
        if (isTimerActive) {
            while (timer > 0) {
                delay(1000L)
                timer--
            }
            isTimerActive = false
            canResend = true
        }
    }

    // Функция для повторной отправки кода
    fun resendCode() {
        if (canResend) {
            onResendClick()
            timer = 60
            isTimerActive = true
            canResend = false
            isError = false // Сбрасываем ошибку при повторной отправке
            errorMessage = ""
        }
    }

    // Функция проверки кода (здесь должна быть логика проверки на сервере)
    fun verifyCode(code: String) {
        // Для примера считаем, что правильный код "123456"
        // В реальном приложении здесь будет запрос к серверу
        val correctCode = "123456"

        if (code == correctCode) {
            isError = false
            onVerifyClick(code)
        } else {
            isError = true
            errorMessage = "Invalid code. Please try again"
        }
    }

    // Форматирование таймера: минуты и секунды
    val minutes = timer / 60
    val seconds = timer % 60
    val timerText = String.format("%02d:%02d", minutes, seconds)

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
                    contentDescription = "Back",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.3f))

        // Заголовок
        Text(
            text = "OTP Verification",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Подзаголовок
        Text(
            text = "Please check your email\nto see the verification code",
            fontSize = 14.sp,
            color = Color.Gray,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Метка "OTP Code" - становится красной при ошибке
        Text(
            text = "OTP Code",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (isError) Color.Red else Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Поле ввода OTP - с поддержкой ошибки
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = otpCode,
            onValueChange = {
                if (it.length <= 6) {
                    otpCode = it
                    isError = false // Сбрасываем ошибку при изменении текста
                    errorMessage = ""
                    if (it.length == 6) {
                        verifyCode(it)
                    }
                }
            },
            placeholderText = "000000",
            isError = isError // Передаем состояние ошибки
        )

        // Сообщение об ошибке (красным цветом)
        if (isError) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = Color.Red,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Таймер
        Text(
            text = timerText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = if (isError) Color.Red else CustomColors.accent // Красный при ошибке
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Ссылка для повторной отправки
        Text(
            text = "Resend code",
            fontSize = 14.sp,
            color = if (canResend) CustomColors.accent else Color.Gray,
            modifier = Modifier
                .clickable(enabled = canResend) { resendCode() }
        )

        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OTPVerificationScreenPreview() {
    OTPVerificationScreen(
        onBackClick = {},
        onVerifyClick = {}
    )
}