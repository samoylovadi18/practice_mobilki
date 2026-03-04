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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.components.AccentButton
import com.example.practice_mobilki.ui.components.CustomAlertDialog
import com.example.practice_mobilki.ui.components.CustomTextField
import com.example.practice_mobilki.ui.components.SuccessDialog
import com.example.practice_mobilki.ui.theme.CustomColors
import com.example.practice_mobilki.ui.viewmodel.ForgotPasswordViewModel

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit,
    onNavigateToOTP: (String) -> Unit,
    viewModel: ForgotPasswordViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    val horizontalPadding = if (isLandscape) 48.dp else 20.dp

    var email by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val isLoading by viewModel.isLoading.collectAsState()
    val isSuccess by viewModel.isSuccess.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val dialogTitle by viewModel.dialogTitle.collectAsState()
    val dialogText by viewModel.dialogText.collectAsState()

    // Функция валидации email
    fun isValidEmail(email: String): Boolean {
        return email.isNotBlank() && email.contains("@") && email.contains(".")
    }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            showSuccessDialog = true
            viewModel.resetState()
        }
    }

    // для портрета и ландшафта
    Column(
        modifier = modifier
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
                    modifier = Modifier.size(20.dp),
                    alpha = 1f
                )
            }
        }

        // Заголовок
        Text(
            text = "Forgot Password",
            fontSize = 32.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center
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
            placeholderText = "xyz@gmail.com",
            isEnabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Индикатор загрузки или кнопка
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = CustomColors.accent,
                    modifier = Modifier.size(30.dp)
                )
            }
        } else {
            // Кнопка "Send"
            AccentButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                text = "Send",
                onClick = {
                    if (isValidEmail(email)) {
                        viewModel.forgotPassword(email)
                    } else {
                        println("Invalid email: $email")
                    }
                }
            )
        }

        // Дополнительный отступ снизу для прокрутки
        Spacer(modifier = Modifier.height(40.dp))
    }

    // Диалог успешной отправки
    SuccessDialog(
        show = showSuccessDialog,
        onDismiss = {
            showSuccessDialog = false
            onNavigateToOTP()
        }
    )

    // Диалог для ошибок
    if (showDialog) {
        CustomAlertDialog(
            show = showDialog,
            onDismiss = { viewModel.hideDialog() },
            text = dialogText,
            title = dialogTitle
        )
    }
}

fun onNavigateToOTP() {
    TODO("Not yet implemented")
}

@Preview(showBackground = true, name = "Портрет")
@Preview(showBackground = true, widthDp = 640, heightDp = 360, name = "Ландшафт")
@Composable
private fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen(
        onBackClick = {},
        onNavigateToOTP = {}
    )
}