package com.example.practice_mobilki.data


import com.example.practice_mobilki.data.service.DataManagementService
import com.example.shoestore.data.service.StorageService
import com.example.practice_mobilki.data.service.UserManagementService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.InetSocketAddress
import java.net.Proxy

object RetrofitInstance {
    const val SUPABASE_URL = "https://lvcfrtimnqfmdbvdsazn.supabase.co"

    private val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress("10.207.106.59", 3128))
    private val client = OkHttpClient.Builder().proxy(proxy).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(SUPABASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val userManagementService = retrofit.create(UserManagementService::class.java)
    val dataManagementService = retrofit.create(DataManagementService::class.java)
    val storageService = retrofit.create(StorageService::class.java)
}

