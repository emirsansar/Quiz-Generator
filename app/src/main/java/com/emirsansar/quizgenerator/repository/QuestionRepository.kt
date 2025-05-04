package com.emirsansar.quizgenerator.repository

import com.emirsansar.quizgenerator.database.QuestionDao
import com.emirsansar.quizgenerator.model.Question

class QuestionRepository(private val questionDao: QuestionDao) {

    suspend fun insertQuestion(question: Question) {
        questionDao.insertQuestion(question)
    }

    suspend fun getAllQuestions(): List<Question> {
        return questionDao.getAllQuestions()
    }

    suspend fun deleteQuestion(question: Question) {
        questionDao.deleteQuestion(question)
    }

    suspend fun deleteAllQuestions() {
        questionDao.deleteAllQuestions()
    }
}
