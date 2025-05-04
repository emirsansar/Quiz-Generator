package com.emirsansar.quizgenerator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.emirsansar.quizgenerator.model.Converters
import com.emirsansar.quizgenerator.model.Question
import com.emirsansar.quizgenerator.model.Quiz

@Database(entities = [Question::class, Quiz::class], version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RoomDB : RoomDatabase() {

    abstract fun questionDao(): QuestionDao
    abstract fun quizDao(): QuizDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDB? = null

        fun getDatabase(context: Context): RoomDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "question_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}