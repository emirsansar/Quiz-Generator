package com.emirsansar.quizgenerator

import android.content.Context
import com.emirsansar.quizgenerator.database.QuestionDao
import com.emirsansar.quizgenerator.database.QuizDao
import com.emirsansar.quizgenerator.database.RoomDB
import com.emirsansar.quizgenerator.repository.QuestionRepository
import com.emirsansar.quizgenerator.repository.QuizRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RoomDB {
        return RoomDB.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideQuestionDao(db: RoomDB): QuestionDao {
        return db.questionDao()
    }

    @Provides
    @Singleton
    fun provideQuizDao(db: RoomDB): QuizDao {
        return db.quizDao()
    }

    @Provides
    @Singleton
    fun provideQuestionRepository(questionDao: QuestionDao): QuestionRepository {
        return QuestionRepository(questionDao)
    }

    @Provides
    @Singleton
    fun provideQuizRepository(quizDao: QuizDao): QuizRepository {
        return QuizRepository(quizDao)
    }
}
