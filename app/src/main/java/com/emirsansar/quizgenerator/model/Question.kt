package com.emirsansar.quizgenerator.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@Entity(tableName = "questions")
@TypeConverters(Converters::class)
data class Question(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val choices: List<String>,
    val answer: String,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable
