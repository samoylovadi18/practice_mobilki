// data/service/DataManagementService.kt

package com.example.practice_mobilki.data.service

import com.example.practice_mobilki.data.model.ProductResponse
import com.example.practice_mobilki.data.model.InsertProfileRequest
import com.example.practice_mobilki.data.model.ProfileResponse
import com.example.practice_mobilki.data.service.API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DataManagementService {

    @Headers(
        "apikey: $API_KEY",
        "Prefer: resolution=merge-duplicates"
    )
    @POST("rest/v1/profiles")
    suspend fun upsertProfile(@Body profile: InsertProfileRequest): Response<Unit>


    @Headers(
        "apikey: $API_KEY",
        "Authorization: Bearer $API_KEY"
    )
    @GET("rest/v1/products")
    suspend fun getProducts(
        @Query("select") select: String = "*",
        @Query("order") order: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null
    ): Response<List<ProductResponse>>

    @Headers(
        "apikey: $API_KEY",
        "Authorization: Bearer $API_KEY"
    )
    @GET("rest/v1/products")
    suspend fun getBestSellers(
        @Query("is_best_seller") isBestSeller: String = "eq.true",
        @Query("select") select: String = "*",
        @Query("limit") limit: Int? = 10
    ): Response<List<ProductResponse>>


}
