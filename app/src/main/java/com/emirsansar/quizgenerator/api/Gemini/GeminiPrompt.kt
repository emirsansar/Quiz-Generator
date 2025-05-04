package com.emirsansar.quizgenerator.utils

object GeminiPrompt {

    fun getPrompt(
        topic: String,
        difficulty: String,
        testType: String,
        language: String,
        count: String
    ): String
    {
        val isMultipleChoice = testType == "Multiple Choice" || testType == "Çoktan Seçmeli"

        return """
            You are a professional AI for generating quiz questions.
        
            🔹 **Task**: Create exactly **$count** **$testType** questions on the topic: **"$topic"**  
            🔹 **Difficulty**: $difficulty  
            🔹 **Language**: Use **only $language** in questions, options, and answers.  
            🔹 **Output Format**: Return a **valid JSON object** in the structure shown below.  
            🔹 **Output Only**: Return **only JSON**. Do not include any explanations, comments, or text outside the JSON.
        
            **Requirements**:
            - Each question must have:
                - A `question` field with the question text.
                - An `options` array with ${if (isMultipleChoice) "4 options labeled A) to D)" else "2 options: A) True, B) False"}.
                - A `correct_option` field that exactly matches one of the option strings.
            - **Do NOT include any explanation, metadata, or preamble.**
        
            **JSON Example Format**:
            {
              "success": "true", // If questions are valid
              
              "error": "", // If there is no error
              
              "questions": [
                {
                  "question": "Question text here",
                  "options": [
                    "A) ${if (isMultipleChoice) "Option 1" else "True"}",
                    "B) ${if (isMultipleChoice) "Option 2" else "False"}"${if (isMultipleChoice) "," else ""}
                    ${if (isMultipleChoice) """
                    "C) Option 3",
                    "D) Option 4"
                    """ else ""}
                  ],
                  "correct_option": "A) ..."
                }
              ]
            }
        
            If the topic is inappropriate, irrelevant, or contains sensitive, obscene, or offensive content, respond with a JSON like this:
        
            {
              "success": "false", // Indicating failure to generate valid questions
              "error": "Topic contains inappropriate content or is irrelevant."
            }
        
            Ensure your output is:
            - Fully parsable as JSON
            - Free of trailing commas
            - Encoded in **UTF-8 plain text**
        """.trimIndent()
    }

}