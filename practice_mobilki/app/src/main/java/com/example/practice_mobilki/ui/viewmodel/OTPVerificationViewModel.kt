package com.example.practice_mobilki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_mobilki.data.RetrofitInstance
import com.example.practice_mobilki.data.model.VerifyOtpRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class OTPVerificationViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _dialogText = MutableStateFlow("")
    val dialogText: StateFlow<String> = _dialogText

    private val _dialogTitle = MutableStateFlow("")
    val dialogTitle: StateFlow<String> = _dialogTitle

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    fun verifyOTP(email: String, token: String, type: String = "signup") {
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = false

            try {
                println("🔐 Verifying OTP - Email: $email, Token: $token, Type: $type")

                val response = RetrofitInstance.userManagementService.verifyOTP(
                    VerifyOtpRequest(
                        email = email,
                        token = token,
                        type = type
                    )
                )

                println("📥 Response code: ${response.code()}")
                println("📥 Response body: ${response.body()}")
                println("📥 Error body: ${response.errorBody()?.string()}")

                if (response.isSuccessful) {
                    println("✅ OTP verification successful")
                    _isSuccess.value = true
                    showSuccessDialog("Успех", "Email успешно подтвержден!")
                } else {
                    when (response.code()) {
                        400 -> {
                            _isError.value = true
                            _errorMessage.value = "Неверный код подтверждения"
                            showErrorDialog("Ошибка", "Неверный код подтверждения")
                        }
                        401 -> {
                            _isError.value = true
                            _errorMessage.value = "Код просрочен"
                            showErrorDialog("Ошибка", "Код просрочен. Запросите новый")
                        }
                        404 -> {
                            _isError.value = true
                            _errorMessage.value = "Пользователь не найден"
                            showErrorDialog("Ошибка", "Пользователь не найден")
                        }
                        else -> {
                            _isError.value = true
                            _errorMessage.value = "Ошибка сервера"
                            showErrorDialog("Ошибка", "Ошибка сервера: ${response.code()}")
                        }
                    }
                }
            } catch (e: Exception) {
                println("❌ Exception: ${e.message}")
                e.printStackTrace()
                _isError.value = true
                _errorMessage.value = e.message ?: "Ошибка сети"
                showErrorDialog("Ошибка", e.message ?: "Ошибка соединения")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resendCode(email: String, type: String = "signup") {
        viewModelScope.launch {
            try {
                println("📤 Resending code for email: $email")
                // Здесь должен быть API запрос на повторную отправку
                showSuccessDialog("Успех", "Код отправлен повторно")
            } catch (e: Exception) {
                showErrorDialog("Ошибка", "Не удалось отправить код")
            }
        }
    }

    private fun showErrorDialog(title: String, message: String) {
        _dialogTitle.value = title
        _dialogText.value = message
        _showDialog.value = true
    }

    private fun showSuccessDialog(title: String, message: String) {
        _dialogTitle.value = title
        _dialogText.value = message
        _showDialog.value = true
    }

    fun hideDialog() {
        _showDialog.value = false
    }

    fun resetState() {
        _isSuccess.value = false
        _isError.value = false
        _errorMessage.value = ""
    }
}