package com.emirsansar.quizgenerator.api.Gemini

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

//Gemini API for generating quiz questions.
interface GeminiAPI {
    /**
     * Generates AI-based quiz questions.
     * @param apiKey API authentication key.
     * @param requestBody JSON request body.
     * @return AI-generated quiz response.
     */

    @POST("v1/models/gemini-2.0-flash:generateContent")
    suspend fun generateQuestions(
        @Query("key") apiKey: String,
        @Body requestBody: RequestBody
    ): Response<ResponseBody>
}
