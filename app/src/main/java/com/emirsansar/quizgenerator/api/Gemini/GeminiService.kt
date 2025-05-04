package com.emirsansar.quizgenerator.api.Gemini

import com.emirsansar.quizgenerator.model.Question
import com.emirsansar.quizgenerator.utils.GeminiPrompt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

data class GeminiResult(
    val questions: List<Question>,
    val success: Boolean,
    val error: String?
)

class GeminiService {

    // Sends a request to the Gemini API to generate quiz questions based on the given parameters.
    // Returns GeminiResult -> Contains either a list of questions (if successful) or an error message.
    suspend fun getGeminiAIQuestions(
        apiKey: String,
        topic: String,
        difficulty: String,
        testType: String,
        language: String,
        count: String
    ): GeminiResult {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Create the prompt to send to Gemini.
                val prompt = GeminiPrompt.getPrompt(topic, difficulty, testType, language, count)

                // 2. Build the JSON request body according to Gemini's expected structure.
                val requestBodyJson = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("role", "user")
                            put("parts", JSONArray().apply {
                                put(JSONObject().apply {
                                    put("text", prompt)
                                })
                            })
                        })
                    })
                }.toString()

                // 3. Send the request.
                val requestBody = RequestBody.create("application/json".toMediaType(), requestBodyJson)
                val response = RetrofitClient.instance.generateQuestions(apiKey, requestBody)

                // 4. Handle non-successful responses.
                if (!response.isSuccessful) {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    throw Exception("API request failed with code: ${response.code()}, message: $errorBody")
                }

                // 5. Parse the response body.
                val responseBody = response.body()?.string() ?: throw Exception("Response body is null")

                // 6. Extract and clean the JSON part returned by Gemini.
                val extractedJson = extractGeminiQuestions(responseBody)

                // 7. Parse the JSON and return result.
                parseGeminiResponse(extractedJson)
            }
            catch (e: Exception) {
                GeminiResult(emptyList(), success = false, error = e.message)
            }
        }
    }

    // Extracts and cleans the question JSON from Gemini's nested API response.
    // Returns a valid JSON string or "{}" if parsing fails.
    private fun extractGeminiQuestions(jsonString: String): String {
        return try {
            val jsonObject = JSONObject(jsonString)

            // Step 1: Access the 'candidates' array.
            val candidatesArray = jsonObject.optJSONArray("candidates")
                ?: throw Exception("API response does not contain 'candidates' field!")

            // Step 2: Get the first candidate's 'content' object.
            val contentObject = candidatesArray.getJSONObject(0).optJSONObject("content")
                ?: throw Exception("API response does not contain 'content' field!")

            // Step 3: Access the 'parts' array which contains the actual text.
            val partsArray = contentObject.optJSONArray("parts")
                ?: throw Exception("API response does not contain 'parts' field!")

            // Step 4: Extract the first 'text' field inside 'parts'.
            val contentText = partsArray.getJSONObject(0).optString("text", "")
            if (contentText.isEmpty()) {
                throw Exception("Extracted text content is empty!")
            }
            println("✅ Extracted 'text' content:\n$contentText")

            // Clean any formatting like ```json from the text.
            val cleanedJson = cleanJsonResponse(contentText)
            println("✅ Cleaned JSON:\n$cleanedJson")

            // Basic sanity check to ensure it's a JSON object.
            if (!cleanedJson.startsWith("{")) {
                throw Exception("Extracted content is not a valid JSON object!")
            }

            // Return cleaned JSON.
            return cleanedJson
        } catch (e: Exception) { // Print error and fallback to empty JSON.
            println("🚨 JSON Parse Error: ${e.message} - API Response: $jsonString")
            return "{}"
        }
    }


    // Cleans unwanted characters such as triple backticks (```json) from AI response.
    private fun cleanJsonResponse(content: String): String {
        return content.replace(Regex("```json|```"), "").trim()
    }

    // Parses the final JSON string into a GeminiResult object (success/failure).
    private fun parseGeminiResponse(jsonString: String): GeminiResult {
        return try {
            val jsonObject = JSONObject(jsonString)

            // Check if the AI successfully generated questions.
            val success = jsonObject.optString("success") == "true"

            // If an error message exists, capture it.
            val error = jsonObject.optString("error").takeIf { it.isNotBlank() }

            // If generation failed, return empty list with error.
            if (!success) {
                println("⚠️ Gemini returned error: $error")
                GeminiResult(emptyList(), success = false, error = error)
            }
            else { // Parse the questions array.
                val questionsArray = jsonObject.getJSONArray("questions")

                val questions = List(questionsArray.length()) { i ->
                    val questionObj = questionsArray.getJSONObject(i)
                    Question(
                        text = questionObj.getString("question"),
                        choices = List(questionObj.getJSONArray("options").length()) { j ->
                            questionObj.getJSONArray("options").getString(j)
                        },
                        answer = questionObj.getString("correct_option")
                    )
                }
                // Return the successfully parsed questions.
                GeminiResult(questions, success = true, error = null)
            }
        } catch (e: JSONException) { // If any parsing error occurs, return failure result.
            GeminiResult(emptyList(), success = false, error = "JSON parse error: ${e.message}")
        }
    }

}
