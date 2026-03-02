package com.example.practice_mobilki.data.service


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

const val API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImhncG1rZG95aHNjYXVicndzanBnIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU3NTU3MDcsImV4cCI6MjA4MTMzMTcwN30.aDVYCugBs1sOTUQGxZxB8EZ1spxBIXBrHMuVXsAnXcA"

interface UserManagementService {

    @Headers("apikey: $API_KEY")
    @POST("auth/v1/signup")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @Headers("apikey: $API_KEY")
    @POST("auth/v1/verify")
    suspend fun verifyOTP(@Body verifyOtp: VerifyOtpRequest): Response<VerifyOtpResponse>

    @Headers("apikey: $API_KEY", "Authorization: Bearer $API_KEY")
    @POST("auth/v1/token?grant_type=password")
    suspend fun signIn(@Body signInRequest: SignInRequest): Response<SignInResponse>

    @Headers("apikey: $API_KEY")
    @POST("auth/v1/resend")
    suspend fun resendOTP(@Body resendOTPRequest: ResendOTPRequest): Response<Unit>
}