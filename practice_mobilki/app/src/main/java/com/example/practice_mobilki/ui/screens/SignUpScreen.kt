package com.example.shoestore.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSignUpClick: (String, String, String, Boolean) -> Unit = { _, _, _, _ -> },
    onSignInClick: () -> Unit = {}
) {
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isCheckboxChecked by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Верхний отступ
        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка "Назад"
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = modifier
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

        // Центральная часть с формой
        Spacer(modifier = Modifier.weight(0.5f))

        // Заголовки
        Text(
            text = stringResource(R.string.register_account),
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

        // Поле "Имя"
        Column {
            Text(
                text = stringResource(R.string.your_name),
                fontSize = 16.sp,
                style = TypographyApplication.bodyMedium16
            )
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userName,
                onValueChange = { userName = it },
                placeholderText = "xxxxxxxx"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Поле "Email"
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

        // Поле "Пароль"
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
                visualTransformation = if (isPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                placeholder = {
                    Text(
                        text = "••••••••",
                        color = CustomColors.hint
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = if (isPasswordVisible)
                            painterResource(R.drawable.eye_open)
                        else
                            painterResource(R.drawable.eye_close),
                        contentDescription = "Toggle password visibility",
                        modifier = Modifier.clickable {
                            isPasswordVisible = !isPasswordVisible
                        }
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Чекбокс согласия
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomCheckbox(
                    checked = isCheckboxChecked,
                    onCheckedChange = { isCheckboxChecked = it },
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = stringResource(R.string.agree_processing_personal),
                    style = TypographyApplication.bodyRegular16
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка регистрации
        AccentButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            text = stringResource(R.string.sign_up),
            onClick = {
                onSignUpClick(userName, email, password, isCheckboxChecked)
            },
            enabled = isCheckboxChecked
        )

        Spacer(modifier = Modifier.weight(1f))

        // Ссылка на вход
        Text(
            text = stringResource(R.string.have_account),
            modifier = Modifier.clickable { onSignInClick() },
            style = TypographyApplication.bodyRegular16
        )

        // Нижний отступ
        Spacer(modifier = Modifier.height(45.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    SignUpScreen()
}