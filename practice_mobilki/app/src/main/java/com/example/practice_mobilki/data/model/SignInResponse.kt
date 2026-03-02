package com.example.practice_mobilki.data.model
data class SignInResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int,
    val refresh_token: String,
    val user: User
)

data class User(
    val id: String,
    val email: String,
    val created_at: String,
    val phone: String?,
    val user_metadata: Map<String, Any?>?
)
