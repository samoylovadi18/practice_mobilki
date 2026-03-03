package com.example.practice_mobilki.ui.screens

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

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun verifyOTP(email: String, token: String, type: String = "signup") {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                println("🔐 Verifying OTP for email: $email, token: $token")

                val response = RetrofitInstance.userManagementService.verifyOTP(
                    VerifyOtpRequest(
                        email = email,
                        token = token,
                        type = type
                    )
                )

                if (response.isSuccessful) {
                    println("✅ OTP verification successful")
                    _isSuccess.value = true
                    showSuccessDialog("Успех", "Email успешно подтвержден!")
                } else {
                    val errorBody = response.errorBody()?.string()
                    println("❌ OTP verification failed: ${response.code()}")
                    println("❌ Error body: $errorBody")

                    when (response.code()) {
                        400 -> showErrorDialog("Ошибка", "Неверный код подтверждения")
                        401 -> showErrorDialog("Ошибка", "Код просрочен. Запросите новый")
                        404 -> showErrorDialog("Ошибка", "Пользователь не найден")
                        else -> showErrorDialog("Ошибка", "Ошибка сервера: ${response.code()}")
                    }
                }
            } catch (e: IOException) {
                println("❌ Network error: ${e.message}")
                showErrorDialog("Ошибка сети", "Проверьте подключение к интернету")
            } catch (e: Exception) {
                println("❌ Exception: ${e.message}")
                e.printStackTrace()
                showErrorDialog("Ошибка", e.message ?: "Неизвестная ошибка")
            } finally {
                _isLoading.value = false
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
        _errorMessage.value = null
    }
}