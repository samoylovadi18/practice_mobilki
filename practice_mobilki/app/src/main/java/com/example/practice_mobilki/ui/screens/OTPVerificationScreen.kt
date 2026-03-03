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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.components.CustomAlertDialog
import com.example.practice_mobilki.ui.components.CustomTextField
import com.example.practice_mobilki.ui.theme.CustomColors
import com.example.practice_mobilki.ui.viewmodel.OTPVerificationViewModel

@Composable
fun OTPVerificationScreen(
    onBackClick: () -> Unit,
    onVerifySuccess: () -> Unit,
    email: String,
    viewModel: OTPVerificationViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    // Адаптивные размеры ТОЛЬКО для отступов
    val horizontalPadding = if (isLandscape) 48.dp else 20.dp
    val verticalSpacing = if (isLandscape) 16.dp else 8.dp

    val context = LocalContext.current
    var otpCode by remember { mutableStateOf("") }
    var timer by remember { mutableStateOf(60) } // 01:00
    var isTimerActive by remember { mutableStateOf(true) }
    var canResend by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val isLoading = viewModel.isLoading.value
    val isSuccess = viewModel.isSuccess.value
    val showDialog = viewModel.showDialog.value
    val dialogTitle = viewModel.dialogTitle.value
    val dialogText = viewModel.dialogText.value
    val isError = viewModel.isError.value

    // Следим за успешной верификацией
    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            viewModel.resetState()
            onVerifySuccess()
        }
    }

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

    fun resendCode() {
        if (canResend) {
            viewModel.resendCode(email)
            timer = 60
            isTimerActive = true
            canResend = false
        }
    }

    // Форматирование таймера
    val minutes = timer / 60
    val seconds = timer % 60
    val timerText = String.format("%02d:%02d", minutes, seconds)

    // ОДИНАКОВЫЙ ДИЗАЙН для портрета и ландшафта
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(horizontal = horizontalPadding)
            .padding(top = 24.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Кнопка "Назад" слева
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
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

        // Заголовок
        Text(
            text = "OTP Проверка",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Подзаголовок
        Text(
            text = "Пожалуйста, Проверьте Свою Электронную Почту,\nЧтобы Увидеть Код Подтверждения",
            fontSize = 14.sp,
            color = Color.Gray,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Метка "ОТР Код"
        Text(
            text = "ОТР Код",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (isError) Color.Red else Color.Black,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Поле ввода OTP
        CustomTextField(
            modifier = Modifier.fillMaxWidth(),
            value = otpCode,
            onValueChange = {
                if (it.length <= 6) {
                    otpCode = it
                    if (it.length == 6 && !isLoading) {
                        viewModel.verifyOTP(email, it)
                    }
                }
            },
            placeholderText = "000000",
            isError = isError,
            isEnabled = !isLoading
        )

        if (isLoading) {
            Spacer(modifier = Modifier.height(verticalSpacing))
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = CustomColors.accent
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Таймер
        Text(
            text = timerText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = if (isError) Color.Red else CustomColors.accent
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Ссылка для повторной отправки
        Text(
            text = "Отправить код повторно",
            fontSize = 14.sp,
            color = if (canResend && !isLoading) CustomColors.accent else Color.Gray,
            modifier = Modifier
                .clickable(enabled = canResend && !isLoading) { resendCode() }
        )

        // Дополнительный отступ снизу для прокрутки
        Spacer(modifier = Modifier.height(40.dp))
    }

    if (showDialog) {
        CustomAlertDialog(
            show = showDialog,
            onDismiss = { viewModel.hideDialog() },
            text = dialogText,
            title = dialogTitle
        )
    }
}

@Preview(showBackground = true, name = "Портрет")
@Preview(showBackground = true, widthDp = 640, heightDp = 360, name = "Ландшафт")
@Composable
private fun OTPVerificationScreenPreview() {
    OTPVerificationScreen(
        onBackClick = {},
        onVerifySuccess = {},
        email = "test@example.com"
    )
}