package com.example.practice_mobilki.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practice_mobilki.data.model.VerifyOtpRequest
import com.example.practice_mobilki.ui.viewmodel.VerifyOTPViewModel
import com.example.practice_mobilki.R
import com.example.practice_mobilki.data.model.ResendOTPRequest
import com.example.practice_mobilki.ui.components.CustomAlertDialog
import com.example.practice_mobilki.ui.components.OtpTextField
import com.example.practice_mobilki.ui.theme.CustomColors
import com.example.practice_mobilki.ui.theme.TypographyApplication
import kotlinx.coroutines.delay

@Composable
fun OtpVerificationScreen(modifier: Modifier = Modifier, viewModel: VerifyOTPViewModel, onVerifyOTPSuccess: () -> Unit, onBack: () -> Unit) {
    var cardInput1 by remember { mutableStateOf(value = "") }
    var cardInput2 by remember { mutableStateOf(value = "") }
    var cardInput3 by remember { mutableStateOf(value = "") }
    var cardInput4 by remember { mutableStateOf(value = "") }
    var cardInput5 by remember { mutableStateOf(value = "") }
    var cardInput6 by remember { mutableStateOf(value = "") }
    val isVerifiedSuccessful by viewModel.isVerifiedSuccessful
    var otpCode by remember { mutableStateOf("") }
    var remainingSeconds by remember { mutableStateOf(30) }
    var isTimerRunning by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val isLoading by viewModel.isLoading
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "my_app_preferences",
        Context.MODE_PRIVATE
    )
    val userEmail = sharedPreferences.getString("userEmail", "Guest")

    LaunchedEffect(key1 = isTimerRunning) {
        if (isTimerRunning && remainingSeconds > 0) {
            while (remainingSeconds > 0) {
                delay(1000L)
                remainingSeconds--
            }
            isTimerRunning = false
        }
    }

    LaunchedEffect(otpCode) {
        if (otpCode.length == 6) {
            viewModel.verifyOTP(VerifyOtpRequest("email",userEmail.toString(), otpCode))
        }
    }

    LaunchedEffect(isVerifiedSuccessful) {
        if (isVerifiedSuccessful) {
            onVerifyOTPSuccess()
            viewModel.resetVerifyOTPState()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
        .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
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
        Text(text = stringResource(R.string.otp_verification), fontSize = 32.sp, style = TypographyApplication.headingRegular32)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = stringResource(R.string.check_code_verification),style = TypographyApplication.bodyRegular16, fontSize = 16.sp, color = CustomColors.hint, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.weight(0.25f))
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.otp_code), style = TypographyApplication.bodyMedium16, fontSize = 16.sp, color = CustomColors.text, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Absolute.SpaceAround) {
            OtpTextField(
                modifier = Modifier.focusRequester(focusRequesters[0]),
                value = cardInput1,
                onValueChange = { newText ->
                    if (newText.length == 1 && newText.first().isDigit()) {
                        cardInput1 = newText
                        focusRequesters[1].requestFocus()
                    } else if (newText.isEmpty()) {
                        cardInput1 = ""
                    }
                    otpCode = cardInput1 + cardInput2 + cardInput3 + cardInput4 + cardInput5 + cardInput6
                }
            )
            OtpTextField(
                modifier = Modifier.focusRequester(focusRequesters[1]),
                value = cardInput2,
                onValueChange = { newText ->
                    if (newText.length == 1 && newText.first().isDigit()) {
                        cardInput2 = newText
                        focusRequesters[2].requestFocus()
                    } else if (newText.isEmpty()) {
                        cardInput2 = ""
                    }
                    otpCode = cardInput1 + cardInput2 + cardInput3 + cardInput4 + cardInput5 + cardInput6
                }
            )
            OtpTextField(
                modifier = Modifier.focusRequester(focusRequesters[2]),
                value = cardInput3,
                onValueChange = { newText ->
                    if (newText.length == 1 && newText.first().isDigit()) {
                        cardInput3 = newText
                        focusRequesters[3].requestFocus()
                    } else if (newText.isEmpty()) {
                        cardInput3 = ""
                    }
                    otpCode = cardInput1 + cardInput2 + cardInput3 + cardInput4 + cardInput5 + cardInput6
                }
            )
            OtpTextField(
                modifier = Modifier.focusRequester(focusRequesters[3]),
                value = cardInput4,
                onValueChange = { newText ->
                    if (newText.length == 1 && newText.first().isDigit()) {
                        cardInput4 = newText
                        focusRequesters[4].requestFocus()
                    } else if (newText.isEmpty()) {
                        cardInput4 = ""
                    }
                    otpCode = cardInput1 + cardInput2 + cardInput3 + cardInput4 + cardInput5 + cardInput6
                }
            )
            OtpTextField(
                modifier = Modifier.focusRequester(focusRequesters[4]),
                value = cardInput5,
                onValueChange = { newText ->
                    if (newText.length == 1 && newText.first().isDigit()) {
                        cardInput5 = newText
                        focusRequesters[5].requestFocus()
                    } else if (newText.isEmpty()) {
                        cardInput5 = ""
                    }
                    otpCode = cardInput1 + cardInput2 + cardInput3 + cardInput4 + cardInput5 + cardInput6
                }
            )
            OtpTextField(
                modifier = Modifier.focusRequester(focusRequesters[5]),
                value = cardInput6,
                onValueChange = { newText ->
                    if (newText.length == 1 && newText.first().isDigit()) {
                        cardInput6 = newText
                    } else if (newText.isEmpty()) {
                        cardInput6 = ""
                    }
                    otpCode = cardInput1 + cardInput2 + cardInput3 + cardInput4 + cardInput5 + cardInput6
                }
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier.fillMaxWidth()) {
            val formattedTime = remember(remainingSeconds) {
                val minutes = remainingSeconds / 60
                val seconds = remainingSeconds % 60
                String.format("%02d:%02d", minutes, seconds)
            }
            Text(
                text = formattedTime,
                fontSize = 16.sp,
                color = CustomColors.hint
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (!isTimerRunning) {
            TextButton(
                onClick = {
                    remainingSeconds = 30
                    isTimerRunning = true
                    cardInput1 = ""
                    cardInput2 = ""
                    cardInput3 = ""
                    cardInput4 = ""
                    cardInput5 = ""
                    cardInput6 = ""
                    viewModel.resendOTP(ResendOTPRequest(userEmail.toString(), "signup"))
                }
            ) {
                Text("Отправить код повторно")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = if (isLoading) "Loading..." else "", color = CustomColors.hint)
        Spacer(modifier = Modifier.weight(1f))
    }
    CustomAlertDialog(
        show = viewModel.showDialog.value,
        onDismiss = { viewModel.hideDialog() },
        text = viewModel.dialogText.value,
        title = viewModel.dialogTitle.value
    )
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview
@Composable
private fun OtpVerificationScreenPreview() {
    OtpVerificationScreen(
        viewModel = VerifyOTPViewModel(),
        onVerifyOTPSuccess = {},
        onBack = {}
    )
}