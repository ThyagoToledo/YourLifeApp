package com.example.yourlife.data.remote

import com.example.yourlife.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton para gerenciar a instância do Retrofit
 * Configuração centralizada da comunicação com a API
 */
object RetrofitInstance {

    // Base URL vem do BuildConfig (definido no build.gradle.kts)
    private const val BASE_URL = "https://your-life-gamma.vercel.app/api/"

    /**
     * Interceptor para logging (apenas em modo debug)
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    /**
     * Cliente OkHttp configurado
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Instância do Retrofit (lazy initialization)
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Instância do ApiService
     */
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

