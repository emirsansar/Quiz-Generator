package com.emirsansar.quizgenerator.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
@Entity(tableName = "quiz_table")
@TypeConverters(Converters::class)
data class Quiz(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topic: String,
    val questions: List<Question>,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable
