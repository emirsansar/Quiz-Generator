package com.emirsansar.quizgenerator.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emirsansar.quizgenerator.model.Quiz

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: Quiz)

    @Query("SELECT * FROM quiz_table")
    suspend fun getAllQuizzes(): List<Quiz>

    @Delete
    suspend fun deleteQuiz(quiz: Quiz)

    @Query("DELETE FROM quiz_table")
    suspend fun deleteAllQuizzes()
}