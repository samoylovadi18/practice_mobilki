package com.example.practice_mobilki.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_mobilki.data.RetrofitInstance
import com.example.practice_mobilki.data.model.ResendOTPRequest
import com.example.practice_mobilki.data.model.VerifyOtpRequest
import kotlinx.coroutines.launch


class VerifyOTPViewModel : ViewModel() {
    private val viewModelShowDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = viewModelShowDialog

    private val viewModelDialogText = mutableStateOf("")
    val dialogText: State<String> = viewModelDialogText

    private val viewModelDialogTitle = mutableStateOf("")
    val dialogTitle: State<String> = viewModelDialogTitle

    fun showError(text: String, title: String = "Внимание") {
        viewModelDialogText.value = text
        viewModelDialogTitle.value = title
        viewModelShowDialog.value = true
    }

    private var viewModelIsVerifyOTPSuccessful = mutableStateOf(false)
    val isVerifiedSuccessful: State<Boolean> = viewModelIsVerifyOTPSuccessful

    fun hideDialog() {
        viewModelShowDialog.value = false
    }

    fun resetVerifyOTPState() {
        viewModelIsVerifyOTPSuccessful.value = false
    }

    private val viewModelIsLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = viewModelIsLoading
    fun verifyOTP(verifyOtpRequest: VerifyOtpRequest) {
        if (viewModelIsLoading.value) return

        viewModelIsLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userManagementService.verifyOTP(verifyOtpRequest)
                if (response.isSuccessful) {
                    response.body()?.let {
                        viewModelIsVerifyOTPSuccessful.value = true
                    }
                } else {
                    showError("Некорректный код OTP", title = "Ошибка OTP")
                }
            } catch (e: Exception) {
                val errorText : String = e.message.toString()
                showError("Произошла ошибка: $errorText", title = "Ошибка OTP")
            } finally {
                viewModelIsLoading.value = false
            }
        }
    }

    fun resendOTP(resendOTPRequest: ResendOTPRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userManagementService.resendOTP(resendOTPRequest)
                if (response.isSuccessful) {
                    response.body()?.let {

                    }
                } else {
                    showError("Не удалось отправить новый код OTP", title = "Ошибка OTP")
                }
            } catch (e: Exception) {
                val errorText : String = e.message.toString()
                showError("Произошла ошибка: $errorText", title = "Ошибка OTP")
            } finally {
                viewModelIsLoading.value = false
            }
        }
    }
}