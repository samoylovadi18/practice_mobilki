package com.example.practice_mobilki.ui.screens

import android.annotation.SuppressLint
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice_mobilki.R
import com.example.practice_mobilki.data.model.SignInRequest
import com.example.practice_mobilki.ui.components.AccentButton
import com.example.practice_mobilki.ui.components.CustomAlertDialog
import com.example.practice_mobilki.ui.components.CustomTextField
import com.example.practice_mobilki.ui.theme.CustomColors
import com.example.practice_mobilki.ui.theme.TypographyApplication
import com.example.practice_mobilki.ui.viewmodel.SignInViewModel

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel,
    onSignInSuccess: () -> Unit,
    toSignUpScreen: () -> Unit,
    toForgotPasswordScreen: () -> Unit,
    onBack: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    // Адаптивные размеры ТОЛЬКО для отступов
    val horizontalPadding = if (isLandscape) 48.dp else 20.dp

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isShowPassword by remember { mutableStateOf(false) }
    val isSignInSuccessful by viewModel.isSignInSuccessful
    val context = LocalContext.current
    val isLoading by viewModel.isLoading

    LaunchedEffect(isSignInSuccessful) {
        if (isSignInSuccessful) {
            onSignInSuccess()
            viewModel.resetSignInState()
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
                        .clickable { onBack() },
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

            // Заголовки
            Text(
                text = "Привет!",
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

            Spacer(modifier = Modifier.height(50.dp))

            // Поле "Email"
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Email",
                    fontSize = 16.sp,
                    style = TypographyApplication.bodyMedium16
                )
                Spacer(modifier = Modifier.height(4.dp))
                CustomTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = { email = it },
                    placeholderText = "xyz@gmail.com",
                    isEnabled = !isLoading
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
                    visualTransformation = if (isShowPassword)
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
                            painter = if (isShowPassword)
                                painterResource(R.drawable.eye_open)
                            else
                                painterResource(R.drawable.eye_close),
                            contentDescription = "Показать/скрыть пароль",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    isShowPassword = !isShowPassword
                                }
                        )
                    },
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Ссылка "Восстановить"
            Box(
                contentAlignment = Alignment.CenterEnd,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Восстановить",
                    fontSize = 14.sp,
                    color = CustomColors.hint,
                    style = TypographyApplication.bodyRegular12,
                    modifier = Modifier.clickable {
                        toForgotPasswordScreen()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка входа
            AccentButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                text = "Войти",
                onClick = {
                    when {
                        email.isBlank() -> {
                            viewModel.showError(
                                "Поле email не может быть пустым",
                                "Ошибка валидации"
                            )
                        }
                        password.isBlank() -> {
                            viewModel.showError(
                                "Поле пароля не может быть пустым",
                                "Ошибка валидации"
                            )
                        }
                        !validateEmail(email) -> {
                            viewModel.showError(
                                "Некорректный email. Email должен соответствовать формату: name@domenname.ru (только маленькие буквы и цифры, домен верхнего уровня больше 2 символов)",
                                "Ошибка валидации"
                            )
                        }
                        password.length < 6 -> {
                            viewModel.showError(
                                "Пароль должен содержать минимум 6 символов",
                                "Ошибка валидации"
                            )
                        }
                        else -> {
                            viewModel.signIn(SignInRequest(email, password), context)
                        }
                    }
                }
            )

            // Индикация загрузки
            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Загрузка...",
                    color = CustomColors.hint,
                    fontSize = 16.sp
                )
            }

            // Добавляем дополнительный отступ снизу для скролла
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Нижняя часть с текстом "Вы впервые? Создать"
        Text(
            text = "Вы впервые? Создать",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .clickable {
                    toSignUpScreen()
                },
            style = TypographyApplication.bodyRegular16
        )
    }

    // Диалог для ошибок
    CustomAlertDialog(
        show = viewModel.showDialog.value,
        onDismiss = { viewModel.hideDialog() },
        text = viewModel.dialogText.value,
        title = viewModel.dialogTitle.value
    )
}

// Функция валидации email
fun validateEmail(email: String): Boolean {
    val pattern = "^[a-z0-9]+@[a-z0-9]+\\.[a-z]{3,}$".toRegex()
    return pattern.matches(email)
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "Портрет")
@Preview(showBackground = true, widthDp = 640, heightDp = 360, name = "Ландшафт")
@Composable
private fun SignInScreenPreview() {
    SignInScreen(
        viewModel = SignInViewModel(),
        onSignInSuccess = {},
        toSignUpScreen = {},
        toForgotPasswordScreen = {},
        onBack = {}
    )
}