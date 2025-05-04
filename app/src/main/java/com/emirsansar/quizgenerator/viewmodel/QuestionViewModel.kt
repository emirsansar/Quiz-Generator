package com.emirsansar.quizgenerator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirsansar.quizgenerator.model.Question
import com.emirsansar.quizgenerator.repository.QuestionRepository
import com.emirsansar.quizgenerator.view.main.component.SortingOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val repository: QuestionRepository
) : ViewModel()
{
    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> get() = _questions

    init {
        fetchQuestions()
    }


    fun fetchQuestions(){
        viewModelScope.launch {
            val questionList = repository.getAllQuestions()
            _questions.postValue(questionList.sortedByDescending { it.createdAt } )
        }
    }

    fun sortQuestionsByDate(sortingOder: SortingOption) {
        viewModelScope.launch {
            val currentList = _questions.value ?: emptyList()

            val sortedList = when (sortingOder) {
                SortingOption.Oldest -> currentList.sortedBy { it.createdAt }
                SortingOption.Newest ->currentList.sortedByDescending { it.createdAt }
            }

            _questions.postValue(sortedList)
        }
    }

    fun insertQuestion(question: Question) {
        viewModelScope.launch {
            repository.insertQuestion(question)
            fetchQuestions()
        }
    }

    fun deleteQuestion(question: Question){
        viewModelScope.launch {
            repository.deleteQuestion(question)
            fetchQuestions()
        }
    }

    fun deleteAllQuestions() {
        viewModelScope.launch {
            repository.deleteAllQuestions()
        }
    }
}