package com.example.practice_mobilki.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practice_mobilki.data.RetrofitInstance
import com.example.practice_mobilki.data.model.InsertProfileRequest
import com.example.practice_mobilki.data.model.ProfileResponse
import com.example.practice_mobilki.data.service.API_KEY
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLException
import kotlin.collections.isEmpty
import kotlin.jvm.java
import kotlin.text.isNullOrBlank
import kotlin.text.isNullOrEmpty

data class ProfileUiState(
    val firstName: String = "",
    val lastname: String = "",
    val phone: String = "",
    val address: String = "",
    val avatarUrl: String? = null,
    val isLoading: Boolean = false,
    val message: String? = null
)

class ProfileViewModel : ViewModel() {
    private val viewModelUiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> = viewModelUiState
    private val viewModelShowDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = viewModelShowDialog

    private val viewModelDialogText = mutableStateOf("")
    val dialogText: State<String> = viewModelDialogText

    private val viewModelDialogTitle = mutableStateOf("")
    val dialogTitle: State<String> = viewModelDialogTitle

    fun showMessage(text: String, title: String = "Внимание") {
        viewModelDialogText.value = text
        viewModelDialogTitle.value = title
        viewModelShowDialog.value = true
    }

    fun hideDialog() {
        viewModelShowDialog.value = false
    }

    private val viewModelProfileImage = mutableStateOf<Bitmap?>(null)
    val profileImage: State<Bitmap?> = viewModelProfileImage
    private val viewModelUiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> = viewModelUiEvent.receiveAsFlow()

    open class UiEvent {
        object OpenCamera : UiEvent()
        data class ImageLoaded(val bitmap: Bitmap) : UiEvent()
    }

    fun onOpenCamera() {
        viewModelUiEvent.trySend(UiEvent.OpenCamera)
    }

    fun saveProfileImage(bitmap: Bitmap) {
        viewModelProfileImage.value = bitmap
        viewModelUiEvent.trySend(UiEvent.ImageLoaded(bitmap))
    }


    // ProfileViewModel.kt
    fun upsertProfile(
        userId: String,
        firstname: String,
        lastname: String,
        address: String,
        phone: String,
        context: Context
    ) {
        viewModelScope.launch {
            try {
                Log.d("ProfileVM", "Начало upsert. userId = $userId")
                Log.d("ProfileVM", "Данные: firstname=$firstname, lastname=$lastname, phone=$phone, address=$address")

                val request = InsertProfileRequest(
                    userId = userId,
                    firstname = firstname,
                    lastname = lastname,
                    address = address,
                    phone = phone,
                    photo = null // добавьте если поле есть
                )

                Log.d("ProfileVM", "Request: $request")

                val upsertResponse = RetrofitInstance.dataManagementService.upsertProfile(request)

                Log.d("ProfileVM", "Код ответа: ${upsertResponse.code()}")
                Log.d("ProfileVM", "Успешно: ${upsertResponse.isSuccessful}")
                Log.d("ProfileVM", "Сообщение: ${upsertResponse.message()}")

                if (upsertResponse.isSuccessful) {
                    Log.d("ProfileVM", "Профиль успешно сохранен")
                    showMessage("Профиль сохранен", "Успех")

                    // Обновляем UI состояние после успешного сохранения
                    viewModelUiState.update {
                        it.copy(
                            firstName = firstname,
                            lastname = lastname,
                            address = address,
                            phone = phone
                        )
                    }
                } else {
                    val errorBody = upsertResponse.errorBody()?.string()
                    Log.e("ProfileVM", "Ошибка профиля: код=${upsertResponse.code()}, body=$errorBody")
                    showMessage("Ошибка сохранения: ${upsertResponse.code()}", "Ошибка")
                }

            } catch (e: Exception) {
                Log.e("ProfileVM", "Ошибка при сохранении профиля", e)
                showMessage("Ошибка: ${e.message}", "Ошибка сети")
            }
        }
    }

    //private val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("10.207.106.71", 3128))

    fun loadProfile(userId: String?) {
        viewModelScope.launch {
            viewModelUiState.update { it.copy(isLoading = true) }

            if (userId.isNullOrEmpty()) {
                Log.e("loadProfile", "userId is null or empty")
                viewModelUiState.update { it.copy(isLoading = false) }
                return@launch
            }

            val request = Request.Builder()
                .url("https://hgpmkdoyhscaubrwsjpg.supabase.co/rest/v1/profiles?user_id=eq.$userId")
                .get()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", "Bearer $API_KEY") // Добавьте эту строку
                .build()

            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
//                .proxy(proxy)
                .build()

            try {
                val response = withContext(Dispatchers.IO) {
                    client.newCall(request).execute()
                }

                Log.d("loadProfile", "HTTP Code: ${response.code}")

                if (!response.isSuccessful) {
                    Log.e("loadProfile", "Server error: ${response.code}")
                    viewModelUiState.update { it.copy(isLoading = false) }
                    return@launch
                }

                val body = response.body?.string()

                if (body.isNullOrBlank()) {
                    Log.d("loadProfile", "Profile not found, creating new one")
                    viewModelUiState.update {
                        it.copy(
                            firstName = "",
                            lastname = "",
                            address = "",
                            phone = "",
                            isLoading = false
                        )
                    }
                    return@launch
                }

                val gson = Gson()
                val profiles: Array<ProfileResponse> = gson.fromJson(body, Array<ProfileResponse>::class.java)

                if (profiles.isEmpty()) {
                    Log.d("loadProfile", "No profile found in array")
                    viewModelUiState.update {
                        it.copy(
                            firstName = "",
                            lastname = "",
                            address = "",
                            phone = "",
                            isLoading = false
                        )
                    }
                    return@launch
                }

                val profile = profiles[0]

                viewModelUiState.update {
                    it.copy(
                        firstName = profile.firstname ?: "",
                        lastname = profile.lastname ?: "",
                        address = profile.address ?: "",
                        phone = profile.phone ?: "",
                        avatarUrl = profile.photo,
                        isLoading = false
                    )
                }

                Log.d("loadProfile", "Profile loaded successfully: $profile")

            } catch (e: Exception) {
                Log.e("loadProfile", "Error: ${e.message}", e)
                viewModelUiState.update { it.copy(isLoading = false) }
                showMessage("Ошибка загрузки профиля: ${e.message}", "Ошибка")
            }
        }
    }


}