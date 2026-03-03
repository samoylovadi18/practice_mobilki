package com.example.practice_mobilki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_mobilki.data.RetrofitInstance
import com.example.practice_mobilki.data.model.ForgotPasswordRequest
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

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val response = RetrofitInstance.userManagementService.forgotPassword(
                    ForgotPasswordRequest(email = email)
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.success == true) {
                        _isSuccess.value = true
                    } else {
                        val errorMsg = body?.message ?: "Ошибка при отправке запроса"
                        showErrorDialog("Ошибка", errorMsg)
                    }
                } else {
                    showErrorDialog("Ошибка", "Сервер вернул ошибку: ${response.code()}")
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

    fun hideDialog() {
        _showDialog.value = false
    }

    fun resetState() {
        _isSuccess.value = false
        _errorMessage.value = null
    }
}