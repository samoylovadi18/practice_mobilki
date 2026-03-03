package com.example.practice_mobilki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_mobilki.data.RetrofitInstance
import com.example.practice_mobilki.data.model.ForgotPasswordRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class ForgotPasswordViewModel : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private val _dialogText = MutableStateFlow("")
    val dialogText: StateFlow<String> = _dialogText

    private val _dialogTitle = MutableStateFlow("")
    val dialogTitle: StateFlow<String> = _dialogTitle

    // Упрощенная защита от частых запросов
    private var lastRequestTime = 0L
    private val minRequestInterval = 60000L // 60 секунд между запросами

    fun forgotPassword(email: String) {
        val currentTime = System.currentTimeMillis()

        // Проверяем, не слишком ли часто отправляем запросы
        if (currentTime - lastRequestTime < minRequestInterval && lastRequestTime != 0L) {
            val remainingSeconds = (minRequestInterval - (currentTime - lastRequestTime)) / 1000
            showErrorDialog(
                "Слишком часто",
                "Пожалуйста, подождите $remainingSeconds сек. перед следующим запросом"
            )
            return
        }

        lastRequestTime = currentTime

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // Небольшая задержка перед запросом
                delay(1000)

                val response = RetrofitInstance.userManagementService.forgotPassword(
                    ForgotPasswordRequest(email = email)
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.success == true) {
                        _isSuccess.value = true
                        showSuccessDialog("Успех", "Код восстановления отправлен на ваш email")
                    } else {
                        val errorMsg = body?.message ?: "Ошибка при отправке запроса"
                        showErrorDialog("Ошибка", errorMsg)
                    }
                } else {
                    when (response.code()) {
                        429 -> showErrorDialog(
                            "Слишком много запросов",
                            "Сервер временно заблокировал запросы. Попробуйте через несколько минут."
                        )
                        404 -> showErrorDialog(
                            "Пользователь не найден",
                            "Пользователь с таким email не зарегистрирован"
                        )
                        else -> showErrorDialog("Ошибка", "Сервер вернул ошибку: ${response.code()}")
                    }
                }
            } catch (e: IOException) {
                showErrorDialog("Ошибка сети", "Проверьте подключение к интернету")
            } catch (e: Exception) {
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