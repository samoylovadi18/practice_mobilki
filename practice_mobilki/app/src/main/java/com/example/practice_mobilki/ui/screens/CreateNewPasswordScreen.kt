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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.components.AccentButton
import com.example.practice_mobilki.ui.theme.CustomColors
import com.example.practice_mobilki.ui.theme.TypographyApplication

@Composable
fun CreateNewPasswordScreen(
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val horizontalPadding = if (isLandscape) 48.dp else 20.dp

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    // Состояния для ошибок
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmPasswordError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Верхняя часть с контентом (скроллируемая)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = horizontalPadding)
                .padding(top = 24.dp),
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
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.back),
                        contentDescription = "Назад",
                        modifier = Modifier.size(20.dp),
                        alpha = 1f
                    )
                }
            }

            // Заголовки
            Text(
                text = "Задать Новый Пароль",
                fontSize = 32.sp,
                style = TypographyApplication.headingRegular32
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Установите новый пароль для входа в вашу учетную запись",
                fontSize = 16.sp,
                color = CustomColors.hint,
                style = TypographyApplication.bodyRegular16,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Поле "Пароль"
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Пароль",
                    fontSize = 16.sp,
                    style = TypographyApplication.bodyMedium16,
                    color = if (passwordError != null) Color.Red else CustomColors.text
                )
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = when {
                            it.length < 6 -> "Пароль должен содержать минимум 6 символов"
                            else -> null
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = CustomColors.background,
                        focusedContainerColor = CustomColors.background,
                        focusedIndicatorColor = if (passwordError != null) Color.Red else Color.Transparent,
                        unfocusedIndicatorColor = if (passwordError != null) Color.Red else Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    visualTransformation = if (isPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    placeholder = {
                        Text(
                            text = "••••••••",
                            color = CustomColors.hint,
                            fontSize = 16.sp
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = if (isPasswordVisible)
                                painterResource(R.drawable.eye_open)
                            else
                                painterResource(R.drawable.eye_close),
                            contentDescription = "Показать/скрыть пароль",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    isPasswordVisible = !isPasswordVisible
                                }
                        )
                    },
                    isError = passwordError != null
                )
                if (passwordError != null) {
                    Text(
                        text = passwordError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Поле "Подтверждение пароля"
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Подтверждение пароля",
                    fontSize = 16.sp,
                    style = TypographyApplication.bodyMedium16,
                    color = if (confirmPasswordError != null) Color.Red else CustomColors.text
                )
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = when {
                            it != password -> "Пароли не совпадают"
                            else -> null
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = CustomColors.background,
                        focusedContainerColor = CustomColors.background,
                        focusedIndicatorColor = if (confirmPasswordError != null) Color.Red else Color.Transparent,
                        unfocusedIndicatorColor = if (confirmPasswordError != null) Color.Red else Color.Transparent,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    visualTransformation = if (isConfirmPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    placeholder = {
                        Text(
                            text = "••••••••",
                            color = CustomColors.hint,
                            fontSize = 16.sp
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = if (isConfirmPasswordVisible)
                                painterResource(R.drawable.eye_open)
                            else
                                painterResource(R.drawable.eye_close),
                            contentDescription = "Показать/скрыть пароль",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    isConfirmPasswordVisible = !isConfirmPasswordVisible
                                }
                        )
                    },
                    isError = confirmPasswordError != null
                )
                if (confirmPasswordError != null) {
                    Text(
                        text = confirmPasswordError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка "Сохранить"
            AccentButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                text = "Сохранить",
                onClick = {
                    // Проверяем все условия перед переходом
                    val isPasswordValid = password.length >= 6
                    val isPasswordsMatch = password == confirmPassword && password.isNotBlank()

                    // Устанавливаем ошибки
                    passwordError = if (!isPasswordValid) "Пароль должен содержать минимум 6 символов" else null
                    confirmPasswordError = if (!isPasswordsMatch) "Пароли не совпадают" else null

                    // Если все хорошо - переходим дальше
                    if (isPasswordValid && isPasswordsMatch) {
                        onSaveSuccess()
                    }
                },
                enabled = password.length >= 6 && confirmPassword == password && password.isNotBlank()
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Нижний отступ
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateNewPasswordScreenPreview() {
    CreateNewPasswordScreen(
        onBack = {},
        onSaveSuccess = {}
    )
}