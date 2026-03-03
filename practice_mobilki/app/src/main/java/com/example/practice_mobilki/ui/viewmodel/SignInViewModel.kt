package com.example.practice_mobilki.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.practice_mobilki.data.RetrofitInstance
import com.example.practice_mobilki.data.model.SignInRequest
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SignInViewModel : ViewModel() {
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

    private var viewModelIsSignInSuccessful = mutableStateOf(false)
    val isSignInSuccessful: State<Boolean> = viewModelIsSignInSuccessful

    fun hideDialog() {
        viewModelShowDialog.value = false
    }

    fun resetSignInState() {
        viewModelIsSignInSuccessful.value = false
    }

    private val viewModelIsLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = viewModelIsLoading

    fun signIn(signInRequest: SignInRequest, context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "my_app_preferences",
            Context.MODE_PRIVATE
        )
        if (viewModelIsLoading.value) return
        viewModelIsLoading.value = true

        viewModelScope.launch {
            try {
                println("🔐 SignIn attempt with email: ${signInRequest.email}")
                println("📤 Request: $signInRequest")

                val response = RetrofitInstance.userManagementService.signIn(signInRequest)

                println("📥 Response code: ${response.code()}")
                println("📥 Response headers: ${response.headers()}")

                if (response.isSuccessful) {
                    val signInResponse = response.body()
                    println("✅ SignIn successful: $signInResponse")

                    signInResponse?.let {
                        val userId = it.user?.id
                        if (userId != null) {
                            sharedPreferences.edit().apply {
                                putString("userId", userId)
                                putString("userEmail", it.user.email)
                                apply()
                            }
                            println("💾 User saved to SharedPreferences: $userId")
                            viewModelIsSignInSuccessful.value = true
                        } else {
                            println("❌ User ID is null in response")
                            showError("Ошибка при получении данных пользователя", "Ошибка")
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    println("❌ SignIn failed: ${response.code()}")
                    println("❌ Error body: $errorBody")

                    when (response.code()) {
                        400 -> {
                            // Проверяем, может быть это ошибка подтверждения email
                            if (errorBody?.contains("Email not confirmed") == true) {
                                showError(
                                    "Email не подтвержден. Проверьте вашу почту и перейдите по ссылке подтверждения",
                                    "Требуется подтверждение"
                                )
                            } else {
                                showError(
                                    "Неверный email или пароль. Проверьте введенные данные",
                                    "Ошибка входа"
                                )
                            }
                        }
                        401 -> showError(
                            "Неверный email или пароль",
                            "Ошибка авторизации"
                        )
                        403 -> showError(
                            "Доступ запрещен. Обратитесь в поддержку",
                            "Ошибка доступа"
                        )
                        404 -> showError(
                            "Пользователь не найден",
                            "Ошибка входа"
                        )
                        422 -> showError(
                            "Email не подтвержден. Проверьте почту",
                            "Требуется подтверждение"
                        )
                        429 -> showError(
                            "Слишком много попыток входа. Подождите 5 минут",
                            "Превышен лимит"
                        )
                        500, 502, 503, 504 -> showError(
                            "Сервер временно недоступен. Попробуйте позже",
                            "Ошибка сервера"
                        )
                        else -> showError(
                            "Ошибка сервера: ${response.code()}. Попробуйте позже",
                            "Ошибка"
                        )
                    }
                }
            } catch (e: UnknownHostException) {
                println("❌ No internet connection: ${e.message}")
                showError(
                    "Отсутствует подключение к интернету. Проверьте сеть",
                    "Ошибка сети"
                )
            } catch (e: SocketTimeoutException) {
                println("❌ Connection timeout: ${e.message}")
                showError(
                    "Сервер не отвечает. Проверьте подключение",
                    "Таймаут соединения"
                )
            } catch (e: IOException) {
                println("❌ Network error: ${e.message}")
                e.printStackTrace()
                showError(
                    "Ошибка сети: ${e.message}",
                    "Ошибка соединения"
                )
            } catch (e: Exception) {
                println("❌ Exception: ${e.message}")
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