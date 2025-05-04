package com.emirsansar.quizgenerator.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// Converts complex types (like List<Question>) to JSON strings and back for Room database storage.
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromQuestionList(list: List<Question>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toQuestionList(json: String): List<Question> {
        val type = object : TypeToken<List<Question>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(json: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }
}

