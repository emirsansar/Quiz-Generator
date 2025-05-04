package com.emirsansar.quizgenerator.repository

import com.emirsansar.quizgenerator.database.QuizDao
import com.emirsansar.quizgenerator.model.Quiz

class QuizRepository(
    private val quizDao: QuizDao
) {
    suspend fun insertQuiz(quiz: Quiz) {
        quizDao.insertQuiz(quiz)
    }

    suspend fun getAllQuizzes(): List<Quiz> {
        return quizDao.getAllQuizzes()
    }

    suspend fun deleteQuiz(quiz: Quiz) {
        quizDao.deleteQuiz(quiz)
    }

    suspend fun deleteAllQuizzes() {
        quizDao.deleteAllQuizzes()
    }
}
