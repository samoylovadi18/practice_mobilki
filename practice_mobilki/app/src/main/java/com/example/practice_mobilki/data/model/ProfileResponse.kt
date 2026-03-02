package com.example.practice_mobilki.data.model
import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("user_id") val userId: String,
    @SerializedName("firstname") val firstname: String?,
    @SerializedName("lastname") val lastname: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("photo") val photo: String?
)