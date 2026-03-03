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
import com.example.practice_mobilki.ui.components.CustomTextField
import com.example.practice_mobilki.ui.theme.CustomColors

@Composable
fun OTPVerificationScreen(
    onBackClick: () -> Unit,
    onVerifyClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var otpCode by remember { mutableStateOf("") }
    var timer by remember { mutableStateOf(30) }

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

        // Метка "OTP Code"
        Text(
            text = "OTP Code",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
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
                    if (it.length == 6) {
                        onVerifyClick(it)
                    }
                }
            },
            placeholderText = "000000"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Таймер
        Text(
            text = String.format("00:%02d", timer),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = CustomColors.accent
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Ссылка для повторной отправки
        Text(
            text = "Resend code",
            fontSize = 14.sp,
            color = CustomColors.accent,
            modifier = Modifier.clickable {
                println("Resend code")
                timer = 30
            }
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