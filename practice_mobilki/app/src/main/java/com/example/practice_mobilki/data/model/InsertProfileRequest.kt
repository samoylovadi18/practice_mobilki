package com.example.practice_mobilki.data.model

import com.google.gson.annotations.SerializedName

data class InsertProfileRequest(
    @SerializedName("user_id")
    val userId: String,

    @SerializedName("firstname")
    val firstname: String,

    @SerializedName("lastname")
    val lastname: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("photo")
    val photo: String? = null
)
