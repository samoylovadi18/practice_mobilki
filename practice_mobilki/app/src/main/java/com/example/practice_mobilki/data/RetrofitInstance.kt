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

    // Флаг для включения/выключения прокси
    private const val USE_PROXY = false // Изменить на true для использования прокси

    // Настройки прокси
    private const val PROXY_HOST = "10.207.106.59"
    private const val PROXY_PORT = 3128

    private val client: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()

        if (USE_PROXY) {
            val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(PROXY_HOST, PROXY_PORT))
            builder.proxy(proxy)
        }

        builder.build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(SUPABASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val userManagementService: UserManagementService by lazy {
        retrofit.create(UserManagementService::class.java)
    }

    val dataManagementService: DataManagementService by lazy {
        retrofit.create(DataManagementService::class.java)
    }

    val storageService: StorageService by lazy {
        retrofit.create(StorageService::class.java)
    }

    // Функция для динамического переключения прокси
    fun setProxyUsage(useProxy: Boolean) {
        // Note: This requires reinitialization of the client and retrofit
        // You might need to implement a way to recreate these instances
    }
}