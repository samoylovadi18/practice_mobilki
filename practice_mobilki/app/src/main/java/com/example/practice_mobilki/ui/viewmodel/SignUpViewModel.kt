package com.example.practice_mobilki.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_mobilki.data.RetrofitInstance
import com.example.practice_mobilki.data.model.SignUpRequest
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class SignUpViewModel : ViewModel() {
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

    private var viewModelIsSignUpSuccessful = mutableStateOf(false)
    val isSignUpSuccessful: State<Boolean> = viewModelIsSignUpSuccessful

    fun hideDialog() {
        viewModelShowDialog.value = false
    }

    fun resetSignUpState() {
        viewModelIsSignUpSuccessful.value = false
    }

    private val viewModelIsLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = viewModelIsLoading

    fun signUp(signUpRequest: SignUpRequest, context: Context) {
        if (viewModelIsLoading.value) return

        viewModelIsLoading.value = true

        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "my_app_preferences",
            Context.MODE_PRIVATE
        )

        viewModelScope.launch {
            try {
                println("📤 SignUp request for email: ${signUpRequest.email}")

                val response = RetrofitInstance.userManagementService.signUp(signUpRequest)

                println("📥 Response code: ${response.code()}")

                if (response.isSuccessful) {
                    response.body()?.let {
                        sharedPreferences.edit().apply {
                            putString("userEmail", signUpRequest.email)
                            putString("userId", response.body()?.id)
                            apply()
                        }
                        println("✅ SignUp successful")
                        viewModelIsSignUpSuccessful.value = true
                    }
                } else {
                    // Обработка ошибок от сервера
                    val errorBody = response.errorBody()?.string()
                    println("❌ SignUp failed: ${response.code()}")
                    println("❌ Error body: $errorBody")

                    when (response.code()) {
                        400 -> showError(
                            "Некорректные данные. Проверьте email и пароль",
                            "Ошибка регистрации"
                        )
                        409 -> showError(
                            "Пользователь с таким email уже существует",
                            "Ошибка регистрации"
                        )
                        422 -> showError(
                            "Email не подтвержден. Проверьте почту",
                            "Требуется подтверждение"
                        )
                        429 -> showError(
                            "Слишком много запросов. Попробуйте позже",
                            "Ошибка"
                        )
                        500, 502, 503, 504 -> showError(
                            "Сервер временно недоступен. Попробуйте позже",
                            "Ошибка сервера"
                        )
                        else -> showError(
                            "Ошибка сервера: ${response.code()}",
                            "Ошибка регистрации"
                        )
                    }
                }
            } catch (e: UnknownHostException) {
                // Нет подключения к интернету
                println("❌ No internet connection: ${e.message}")
                showError(
                    "Отсутствует подключение к интернету. Проверьте сеть и попробуйте снова",
                    "Ошибка сети"
                )
            } catch (e: SocketTimeoutException) {
                // Таймаут соединения
                println("❌ Connection timeout: ${e.message}")
                showError(
                    "Сервер не отвечает. Проверьте подключение и попробуйте снова",
                    "Таймаут соединения"
                )
            } catch (e: IOException) {
                // Другие сетевые ошибки
                println("❌ Network error: ${e.message}")
                showError(
                    "Ошибка сети: ${e.message}. Проверьте подключение к интернету",
                    "Ошибка соединения"
                )
            } catch (e: Exception) {
                // Непредвиденные ошибки
                println("❌ Unexpected error: ${e.message}")
                e.printStackTrace()
                showError(
                    "Произошла непредвиденная ошибка: ${e.message}",
                    "Ошибка"
                )
            } finally {
                viewModelIsLoading.value = false
            }
        }
    }
}