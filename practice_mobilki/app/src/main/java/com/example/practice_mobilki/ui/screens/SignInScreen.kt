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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    onBack: () -> Unit
) {
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
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = modifier
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

        Spacer(modifier = Modifier.weight(0.5f))

        Text(
            text = stringResource(R.string.hello_again),
            fontSize = 32.sp,
            style = TypographyApplication.headingRegular32
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.fill_details),
            fontSize = 16.sp,
            color = CustomColors.hint,
            style = TypographyApplication.bodyRegular16
        )

        Spacer(modifier = Modifier.weight(0.25f))

        Column {
            Text(
                text = stringResource(R.string.email),
                fontSize = 16.sp,
                style = TypographyApplication.bodyMedium16
            )
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = { email = it },
                placeholderText = "xyz@gmail.com"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column {
            Text(
                text = stringResource(R.string.password),
                fontSize = 16.sp,
                style = TypographyApplication.bodyMedium16
            )
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
                visualTransformation = if (isShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
                placeholder = { Text(text = "••••••••", color = CustomColors.hint) },
                trailingIcon = {
                    Icon(
                        painter = if (isShowPassword) painterResource(R.drawable.eye_open) else painterResource(R.drawable.eye_close),
                        contentDescription = "An eye",
                        modifier = Modifier.clickable {
                            isShowPassword = !isShowPassword
                        }
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(R.string.recovery_password),
                fontSize = 16.sp,
                color = CustomColors.hint,
                style = TypographyApplication.bodyRegular12
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ⭐ ЗАДАНИЕ 13: Кнопка входа с валидацией на пустоту полей
        AccentButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            text = stringResource(R.string.sign_in),
            onClick = {
                when {
                    // Проверка на пустые поля
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
                    // Проверка email
                    !validateEmail(email) -> {
                        viewModel.showError(
                            "Некорректный email. Email должен соответствовать формату: name@domenname.ru (только маленькие буквы и цифры, домен верхнего уровня больше 2 символов)",
                            "Ошибка валидации"
                        )
                    }
                    // Проверка длины пароля
                    password.length < 6 -> {
                        viewModel.showError(
                            "Пароль должен содержать минимум 6 символов",
                            "Ошибка валидации"
                        )
                    }
                    // Все проверки пройдены
                    else -> {
                        viewModel.signIn(SignInRequest(email, password), context)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = stringResource(R.string.new_user),
            modifier = Modifier.clickable {
                toSignUpScreen()
            },
            style = TypographyApplication.bodyRegular16
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Text(text = "Loading...", color = CustomColors.hint)
        }

        Spacer(modifier = Modifier.height(45.dp))
    }

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
@Preview
@Composable
private fun SignInScreenPreview() {
    SignInScreen(
        viewModel = SignInViewModel(),
        onSignInSuccess = {},
        toSignUpScreen = {},
        onBack = {}
    )
}