package com.example.practice_mobilki.data.model

data class VerifyOtpRequest (
    val type : String,
    val email : String,
    val token : String
)