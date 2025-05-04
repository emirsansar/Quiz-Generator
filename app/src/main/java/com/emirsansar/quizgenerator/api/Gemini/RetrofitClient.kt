package com.emirsansar.quizgenerator.api.Gemini

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

// RetrofitClient is a singleton object that initializes and provides
// an instance of Retrofit for making API requests to Gemini AI.
object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    // Logs API requests and responses.
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient with logging enabled.
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    //Retrofit instance with JSON conversion.
    val instance: GeminiAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiAPI::class.java)
    }
}