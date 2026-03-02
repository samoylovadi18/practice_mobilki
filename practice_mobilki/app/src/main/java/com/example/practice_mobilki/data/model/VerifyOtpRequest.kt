package com.example.myapplication.data.model

data class VerifyOtpRequest (
    val type : String,
    val email : String,
    val token : String
)