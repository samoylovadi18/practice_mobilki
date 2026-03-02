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

class SignUpViewModel : ViewModel() {
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
                val response = RetrofitInstance.userManagementService.signUp(signUpRequest)
                if (response.isSuccessful) {
                    response.body()?.let {
                        sharedPreferences.edit().apply {
                            putString("userEmail", signUpRequest.email)
                            putString("userId", response.body()?.id)
                            apply()
                        }
                        viewModelIsSignUpSuccessful.value = true
                    }
                } else {
                    val messageText : String = response.message().toString()
                    showError("Не удалось зарегистрироваться: $messageText", title = "Ошибка")
                }
            } catch (e: Exception) {
                val errorText : String = e.message.toString()
                showError("Произошла ошибка: $errorText", title = "Ошибка")
            } finally {
                viewModelIsLoading.value = false
            }
        }
    }
}