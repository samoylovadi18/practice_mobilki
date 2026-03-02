package com.example.shoestore.data.service

import com.example.practice_mobilki.data.service.API_KEY
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface StorageService {

    @Headers(
        "apikey: $API_KEY",
        "Authorization: Bearer $API_KEY"
    )
    @Multipart
    @POST("storage/v1/object/avatars/{fileName}") // ← без /public/
    suspend fun uploadPhoto(
        @Path("fileName") fileName: String,
        @Part file: MultipartBody.Part
    ): Response<Unit>
}

