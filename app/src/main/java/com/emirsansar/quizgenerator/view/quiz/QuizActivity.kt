package com.emirsansar.quizgenerator.view.quiz

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.emirsansar.quizgenerator.AppSettings
import com.emirsansar.quizgenerator.BuildConfig
import com.emirsansar.quizgenerator.R
import com.emirsansar.quizgenerator.api.Gemini.GeminiService
import com.emirsansar.quizgenerator.model.Question
import com.emirsansar.quizgenerator.model.Quiz
import com.emirsansar.quizgenerator.ui.theme.QuizGeneratorTheme
import com.emirsansar.quizgenerator.view.quiz.component.LeaveDialog
import com.emirsansar.quizgenerator.viewmodel.QuestionViewModel
import com.emirsansar.quizgenerator.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class QuizActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val topic = intent.getStringExtra("topic") ?: ""
        val difficulty = intent.getStringExtra("difficulty") ?: ""
        val testType = intent.getStringExtra("test_type") ?: ""
        val language = intent.getStringExtra("language") ?: ""
        val count = intent.getStringExtra("question_count") ?: ""

        setContent {
            QuizGeneratorTheme(darkTheme = AppSettings.isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    QuizContainer(
                        topic = topic,
                        difficulty = difficulty,
                        testType = testType,
                        language = language,
                        count = count,
                        onFinish = { finish() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizContainer(
    topic: String,
    difficulty: String,
    testType: String,
    language: String,
    count: String,
    onFinish: () -> Unit
) {
    var questions by remember { mutableStateOf<List<Question>>(emptyList()) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isResultVisible by remember { mutableStateOf(false) }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var isQuizCompleted by remember { mutableStateOf(false) }
    var correctAnswerCount by remember { mutableIntStateOf(0) }
    var favouriteQuestionIds by remember { mutableStateOf(setOf<Int>()) }
    var isLeaveDialogVisible by remember { mutableStateOf(false) }

    var isQuizLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val quizzesViewModel: QuizViewModel = hiltViewModel()
    val questionViewModel: QuestionViewModel = hiltViewModel()

    LaunchedEffect(Unit) {
        try {
            val service = GeminiService()
            val result = withContext(Dispatchers.IO) {
                service.getGeminiAIQuestions(
                    BuildConfig.API_KEY_GEMINI,
                    topic,
                    difficulty,
                    testType,
                    language,
                    count
                )
            }

            withContext(Dispatchers.Main) {
                if (!result.success) {
                    errorMessage = result.error ?: context.getString(R.string.error_unknown)
                } else {
                    questions = result.questions
                    errorMessage = null
                }
                isQuizLoading = false
            }
        } catch (e: Exception) {
            errorMessage = context.getString(R.string.error_unexpected, e.message)
            isQuizLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopBarQuizScreen(
                isQuizCompleted = isQuizCompleted,
                isFavourite = favouriteQuestionIds.contains(currentQuestionIndex),
                onBackPress = { isLeaveDialogVisible = true },
                onFavouriteQuestion = {
                    if (questions.isNotEmpty() && currentQuestionIndex < questions.size) {
                        val question = questions[currentQuestionIndex]

                        if (favouriteQuestionIds.contains(currentQuestionIndex)) {
                            questionViewModel.deleteQuestion(question)

                            favouriteQuestionIds = favouriteQuestionIds.toMutableSet().apply {
                                remove(currentQuestionIndex)
                            }

                            Toast.makeText(context, context.getString(R.string.question_removed_successfully), Toast.LENGTH_SHORT).show()
                        } else {
                            questionViewModel.insertQuestion(question)

                            favouriteQuestionIds = favouriteQuestionIds.toMutableSet().apply {
                                add(currentQuestionIndex)
                            }

                            Toast.makeText(context, context.getString(R.string.question_added_successfully), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            when {
                isQuizLoading -> {
                    LoadingQuizScreen(paddingValues)
                }

                errorMessage != null -> {
                    ErrorQuizScreen(paddingValues, errorMessage!!)
                }

                questions.isNotEmpty() -> {
                    QuizScreen(
                        paddingValues = paddingValues,
                        questions = questions,
                        testCompleted = isQuizCompleted,
                        currentQuestionIndex = currentQuestionIndex,
                        selectedAnswer = selectedAnswer,
                        showResult = isResultVisible,
                        correctAnswers = correctAnswerCount,
                        onAnswerSelected = { answer ->
                            selectedAnswer = answer
                        },
                        onNextQuestion = {
                            if (isResultVisible) {
                                if (selectedAnswer == questions[currentQuestionIndex].answer) {
                                    correctAnswerCount++
                                }

                                if (currentQuestionIndex < questions.size - 1) {
                                    currentQuestionIndex++
                                    selectedAnswer = null
                                    isResultVisible = false
                                } else {
                                    isQuizCompleted = true
                                }
                            } else {
                                isResultVisible = true
                            }
                        },
                        onTestComplete = {
                            isQuizCompleted = true
                        },
                        onSaveQuiz = {
                            quizzesViewModel.insertQuiz(Quiz(topic = topic, questions = questions))
                            AppSettings.isSavedCreatedQuiz = true
                            Toast.makeText(context, context.getString(R.string.quiz_added_successfully), Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            if (isLeaveDialogVisible) {
                LeaveDialog(
                    onConfirm = { onFinish() },
                    onDismiss = { isLeaveDialogVisible = false }
                )
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarQuizScreen(
    isQuizCompleted: Boolean,
    isFavourite: Boolean,
    onBackPress: () -> Unit,
    onFavouriteQuestion: () -> Unit,
) {
    TopAppBar(
        title = { Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Quiz",
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        } },
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back_button_description)
                )
            }
        },
        actions = {
            IconButton(
                onClick = { if (!isQuizCompleted) onFavouriteQuestion() },
                modifier = Modifier.alpha(if (isQuizCompleted) 0f else 1f)
            ) {
                Icon(
                    imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = stringResource(R.string.fav_question_description)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
