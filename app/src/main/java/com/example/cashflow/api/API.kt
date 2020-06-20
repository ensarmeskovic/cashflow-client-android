package com.example.cashflow.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object API {
    private const val ROOT_URL = "https://cashflow.ema.health"
    private const val TIMEOUT_VALUE = 8L

    private fun getInstance(): Retrofit {
        val builder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()).baseUrl(ROOT_URL)

        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .readTimeout(TIMEOUT_VALUE, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_VALUE, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_VALUE, TimeUnit.SECONDS)
            .cache(null)

        builder.client(httpClient.build())
        return builder.build()
    }

    fun getService() = getInstance().create(ApiService::class.java)
}
