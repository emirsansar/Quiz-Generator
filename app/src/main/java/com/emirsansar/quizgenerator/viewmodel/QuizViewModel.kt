package com.emirsansar.quizgenerator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirsansar.quizgenerator.model.Quiz
import com.emirsansar.quizgenerator.repository.QuizRepository
import com.emirsansar.quizgenerator.view.main.component.SortingOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel()
{
     private val _quizzes = MutableLiveData<List<Quiz>>()
     val quizzes: LiveData<List<Quiz>> get() = _quizzes

     init {
         fetchQuizzes()
     }


     fun fetchQuizzes() {
         viewModelScope.launch {
             val quizList = repository.getAllQuizzes()
             _quizzes.postValue(quizList.sortedByDescending { it.createdAt })
         }
     }

     fun sortQuizzesByDate(sortingOrder: SortingOption) {
         viewModelScope.launch {
             val currentList = _quizzes.value ?: emptyList()

             val sortedList = when (sortingOrder) {
                 SortingOption.Oldest -> currentList.sortedBy { it.createdAt }
                 SortingOption.Newest -> currentList.sortedByDescending { it.createdAt }
             }

             _quizzes.postValue(sortedList)
         }
     }

     fun insertQuiz(quiz: Quiz) {
         viewModelScope.launch {
             repository.insertQuiz(quiz)
             fetchQuizzes()
         }
     }

     fun deleteQuiz(quiz: Quiz){
         viewModelScope.launch {
             repository.deleteQuiz(quiz)
             fetchQuizzes()
         }
     }

     fun deleteAllQuizzes() {
         viewModelScope.launch {
             repository.deleteAllQuizzes()
             _quizzes.postValue(emptyList())
         }
     }
}