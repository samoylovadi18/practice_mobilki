package com.example.practice_mobilki.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_mobilki.data.RetrofitInstance
import com.example.practice_mobilki.data.model.ForgotPasswordRequest
import com.example.practice_mobilki.data.model.ForgotPasswordResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ForgotPasswordViewModel : ViewModel() {
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

    // Для защиты от частых запросов
    private var lastRequestTime = 0L
    private val minRequestInterval = 60000L // 60 секунд между запросами
    private var requestCount = 0
    private val maxRequestsPerHour = 5
    private var blockUntil = 0L

    fun forgotPassword(email: String) {
        val currentTime = System.currentTimeMillis()

        // Проверка на блокировку
        if (currentTime < blockUntil) {
            val remainingMinutes = ((blockUntil - currentTime) / 60000) + 1
            showErrorDialog(
                "Слишком много запросов",
                "Достигнут лимит запросов. Пожалуйста, подождите $remainingMinutes мин."
            )
            return
        }

        // Проверка интервала между запросами
        if (currentTime - lastRequestTime < minRequestInterval && lastRequestTime != 0L) {
            val remainingSeconds = (minRequestInterval - (currentTime - lastRequestTime)) / 1000
            showErrorDialog(
                "Слишком часто",
                "Пожалуйста, подождите $remainingSeconds сек. перед следующим запросом"
            )
            return
        }

        // Счетчик запросов
        requestCount++
        if (requestCount > maxRequestsPerHour) {
            blockUntil = currentTime + 3600000 // Блокировка на 1 час
            showErrorDialog(
                "Лимит исчерпан",
                "Вы превысили лимит запросов. Попробуйте через час."
            )
            requestCount = 0
            return
        }

        lastRequestTime = currentTime

        viewModelScope.launch {
            _isLoading.value = true

            try {
                println("📤 Forgot password request for email: $email")

                // Небольшая задержка перед запросом
                delay(1000)

                val response = RetrofitInstance.userManagementService.forgotPassword(
                    ForgotPasswordRequest(email = email)
                )

                println("📥 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.success == true) {
                        println("✅ Forgot password successful")
                        _isSuccess.value = true
                        showSuccessDialog("Успех", "Код восстановления отправлен на ваш email")
                    } else {
                        val errorMsg = body?.message ?: "Ошибка при отправке запроса"
                        showErrorDialog("Ошибка", errorMsg)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    println("❌ Forgot password failed: ${response.code()}")

                    when (response.code()) {
                        400 -> showErrorDialog(
                            "Ошибка запроса",
                            "Некорректный email. Проверьте введенные данные"
                        )
                        404 -> showErrorDialog(
                            "Пользователь не найден",
                            "Пользователь с таким email не зарегистрирован"
                        )
                        422 -> showErrorDialog(
                            "Некорректный email",
                            "Проверьте правильность введенного email"
                        )
                        429 -> {
                            requestCount = maxRequestsPerHour + 1 // Принудительная блокировка
                            showErrorDialog(
                                "Слишком много запросов",
                                "Вы превысили лимит запросов. Попробуйте через час."
                            )
                        }
                        500, 502, 503, 504 -> showErrorDialog(
                            "Ошибка сервера",
                            "Сервер временно недоступен. Попробуйте позже"
                        )
                        else -> showErrorDialog(
                            "Ошибка",
                            "Сервер вернул ошибку: ${response.code()}"
                        )
                    }
                }
            } catch (e: UnknownHostException) {
                println("❌ No internet connection: ${e.message}")
                showErrorDialog(
                    "Ошибка сети",
                    "Отсутствует подключение к интернету. Проверьте сеть и попробуйте снова"
                )
            } catch (e: SocketTimeoutException) {
                println("❌ Connection timeout: ${e.message}")
                showErrorDialog(
                    "Таймаут соединения",
                    "Сервер не отвечает. Проверьте подключение и попробуйте снова"
                )
            } catch (e: IOException) {
                println("❌ Network error: ${e.message}")
                showErrorDialog(
                    "Ошибка сети",
                    "Ошибка соединения: ${e.message}"
                )
            } catch (e: Exception) {
                println("❌ Exception: ${e.message}")
                e.printStackTrace()
                showErrorDialog(
                    "Ошибка",
                    e.message ?: "Неизвестная ошибка"
                )
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
    }
}