package com.example.practice_mobilki.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice_mobilki.R
import com.example.practice_mobilki.ui.components.AccentButton
import com.example.practice_mobilki.ui.components.CustomCheckbox
import com.example.practice_mobilki.ui.components.CustomTextField
import com.example.practice_mobilki.ui.theme.CustomColors
import com.example.practice_mobilki.ui.theme.TypographyApplication
import com.example.practice_mobilki.ui.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel,
    onSignUpSuccess: () -> Unit,
    toSignInScreen: () -> Unit,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    // Адаптивные размеры ТОЛЬКО для отступов
    val horizontalPadding = if (isLandscape) 48.dp else 20.dp

    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isCheckboxChecked by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val isLoading by viewModel.isLoading
    val isSignUpSuccessful by viewModel.isSignUpSuccessful
    val context = LocalContext.current

    // Следим за успешной регистрацией
    androidx.compose.runtime.LaunchedEffect(isSignUpSuccessful) {
        if (isSignUpSuccessful) {
            onSignUpSuccess()
            viewModel.resetSignUpState()
        }
    }

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
                        .clickable { onBackClick() },
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
                text = "Регистрация",
                fontSize = 32.sp,
                style = TypographyApplication.headingRegular32
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Заполните свои данные",
                fontSize = 16.sp,
                color = CustomColors.hint,
                style = TypographyApplication.bodyRegular16
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Поле "Имя"
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Ваше имя",
                    fontSize = 16.sp,
                    style = TypographyApplication.bodyMedium16
                )
                Spacer(modifier = Modifier.height(4.dp))
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = userName,
                    onValueChange = { userName = it },
                    placeholderText = "Иван Иванов"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Поле "Email"
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Электронная почта",
                    fontSize = 16.sp,
                    style = TypographyApplication.bodyMedium16
                )
                Spacer(modifier = Modifier.height(4.dp))
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = { email = it },
                    placeholderText = "example@gmail.com"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Поле "Пароль"
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Пароль",
                    fontSize = 16.sp,
                    style = TypographyApplication.bodyMedium16
                )
                Spacer(modifier = Modifier.height(4.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = { password = it },
                    shape = RoundedCornerShape(14.dp),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = CustomColors.background,
                        focusedContainerColor = CustomColors.background,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
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
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Чекбокс согласия
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CustomCheckbox(
                    checked = isCheckboxChecked,
                    onCheckedChange = { isCheckboxChecked = it },
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Даю согласие на обработку персональных данных",
                    fontSize = 16.sp,
                    style = TypographyApplication.bodyRegular16
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка регистрации
            AccentButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                text = "Зарегистрироваться",
                onClick = {
                    if (userName.isBlank()) {
                        viewModel.showError("Пожалуйста, введите имя")
                    } else if (!isValidEmail(email)) {
                        viewModel.showError("Некорректный email. Email должен соответствовать формату: name@domenname.ru (только маленькие буквы и цифры, домен верхнего уровня больше 2 символов)")
                    } else if (password.length < 6) {
                        viewModel.showError("Пароль должен содержать минимум 6 символов")
                    } else if (!isCheckboxChecked) {
                        viewModel.showError("Необходимо подтвердить согласие на обработку персональных данных")
                    } else {
                        val signUpRequest = com.example.practice_mobilki.data.model.SignUpRequest(
                            email = email,
                            password = password
                        )
                        viewModel.signUp(signUpRequest, context)
                    }
                },
                enabled = isCheckboxChecked
            )

            // Индикация загрузки
            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(
                    color = CustomColors.accent,
                    modifier = Modifier.size(30.dp)
                )
            }

            // Добавляем дополнительный отступ снизу для скролла
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Нижняя часть с текстом "Уже есть аккаунт? Войти"
        Text(
            text = "Уже есть аккаунт? Войти",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .clickable { toSignInScreen() },
            style = TypographyApplication.bodyRegular16
        )
    }
}

/**
 * Функция валидации email
 */
fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$".toRegex()
    return email.matches(emailRegex)
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Портрет")
@Preview(showBackground = true, widthDp = 640, heightDp = 360, name = "Ландшафт")
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen(
        viewModel = SignUpViewModelPreview(),
        onSignUpSuccess = {},
        toSignInScreen = {},
        onBackClick = {}
    )
}

private class SignUpViewModelPreview : SignUpViewModel()