package com.example.practice_mobilki.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.practice_mobilki.data.RetrofitInstance
import com.example.practice_mobilki.data.model.SignInRequest
import com.example.practice_mobilki.data.model.SignUpRequest
import kotlinx.coroutines.launch
import kotlin.apply
import kotlin.let
import kotlin.text.isNotEmpty
import kotlin.toString

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

    fun signIn(signInRequest: SignInRequest, context : Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(
            "my_app_preferences",
            Context.MODE_PRIVATE
        )
        if (viewModelIsLoading.value) return
        viewModelIsLoading.value = true

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.userManagementService.signIn(signInRequest)
                if (response.isSuccessful) {
                    response.body()?.let {
                        val signInResponse = response.body()
                        val userId = signInResponse?.user?.id

                        if (userId != null) {
                            sharedPreferences.edit().apply {
                                putString("userId", userId)
                                putString("userEmail", signInResponse.user.email)
                                apply()
                            }
                            viewModelIsSignInSuccessful.value = true
                        }
                    }
                } else {
                    if(signInRequest.email.isNotEmpty() && signInRequest.password.isNotEmpty())
                        showError("Введены некорректные данные пользователя! Попробуйте ещё разочек", title = "Ошибка")
                    else
                        showError("Должны быть заполнены все поля")
                }
            } catch (e: Exception) {
                val errorText : String = e.message.toString()
                showError("Ошибка: $errorText", title = "Ошибка авторизации")
            } finally {
                viewModelIsLoading.value = false
            }
        }
    }
}