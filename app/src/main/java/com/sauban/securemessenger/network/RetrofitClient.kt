package com.sauban.securemessenger.network

import com.sauban.securemessenger.helper.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Interceptor

object ApiClient {

    private var authToken: String? = null // optional global token

    fun setToken(token: String) {
        authToken = token
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
            authToken?.let { request.addHeader("Authorization", "Bearer $it") }
            chain.proceed(request.build())
        }
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://securefamily.duckdns.org:3786/api/v1/") // base URL
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

