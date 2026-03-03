package com.example.practice_mobilki.data.service

import com.example.practice_mobilki.data.model.ForgotPasswordRequest
import com.example.practice_mobilki.data.model.ForgotPasswordResponse
import com.example.practice_mobilki.data.model.ResendOTPRequest
import com.example.practice_mobilki.data.model.SignInRequest
import com.example.practice_mobilki.data.model.SignInResponse
import com.example.practice_mobilki.data.model.SignUpRequest
import com.example.practice_mobilki.data.model.SignUpResponse
import com.example.practice_mobilki.data.model.VerifyOtpRequest
import com.example.practice_mobilki.data.model.VerifyOtpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imx2Y2ZydGltbnFmbWRidmRzYXpuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Njk2NzE5MTIsImV4cCI6MjA4NTI0NzkxMn0.HkAcHGWKt6V-EzSMYhnwnLToM6K26H1hs4OJ0fF9Lyw"

interface UserManagementService {

    @Headers("apikey: $API_KEY")
    @POST("auth/v1/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @Headers("apikey: $API_KEY")
    @POST("auth/v1/verify")
    suspend fun verifyOTP(@Body verifyOtp: VerifyOtpRequest): Response<VerifyOtpResponse>

    @Headers(
        "apikey: $API_KEY",
        "Content-Type: application/json"
    )
    @POST("auth/v1/token?grant_type=password")
    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInResponse>

    @Headers("apikey: $API_KEY")
    @POST("auth/v1/resend")
    suspend fun resendOTP(@Body resendOTPRequest: ResendOTPRequest): Response<Unit>

    @Headers("apikey: $API_KEY")
    @POST("auth/v1/recover")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): Response<ForgotPasswordResponse>
}