package com.emirsansar.quizgenerator.view.quiz

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emirsansar.quizgenerator.R
import com.emirsansar.quizgenerator.model.Question
import com.emirsansar.quizgenerator.ui.theme.Blue
import com.emirsansar.quizgenerator.ui.theme.CorrectOption
import com.emirsansar.quizgenerator.ui.theme.SelectedOption
import com.emirsansar.quizgenerator.ui.theme.QuizGeneratorTheme
import com.emirsansar.quizgenerator.ui.theme.WrongOption

@Composable
fun QuizScreen(
    paddingValues: PaddingValues,
    questions: List<Question>,
    currentQuestionIndex: Int,
    testCompleted: Boolean,
    selectedAnswer: String?,
    showResult: Boolean,
    correctAnswers: Int,
    onAnswerSelected: (String) -> Unit,
    onNextQuestion: () -> Unit,
    onTestComplete: () -> Unit,
    onSaveQuiz: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(horizontal = 16.dp),
        verticalArrangement = if (testCompleted) Arrangement.Center else Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!testCompleted && questions.isNotEmpty() && currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]

            Column {
                QuestionProgress(
                    currentQuestionIndex = currentQuestionIndex,
                    totalQuestions = questions.size
                )

                Spacer(modifier = Modifier.height(12.dp))

                QuestionCard(
                    question = question,
                    selectedAnswer = selectedAnswer,
                    showResult = showResult,
                    onAnswerSelected = onAnswerSelected
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (showResult) {
                    Text(
                        text = if (selectedAnswer == question.answer) stringResource(id = R.string.answer_correct)
                                                                else stringResource(id = R.string.answer_wrong),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(top = 5.dp, start = 16.dp)
                    )
                }
            }

            ProgressQuizButton(
                showResult = showResult,
                currentQuestionIndex = currentQuestionIndex,
                questionsSize = questions.size,
                selectedAnswer = selectedAnswer,
                onNextQuestion = onNextQuestion,
                onTestComplete = onTestComplete
            )
        }
        else {
            CompletedScreen(
                correctAnswers = correctAnswers,
                questionsSize = questions.size,
                onSaveQuiz = onSaveQuiz,
                paddingValues = paddingValues
            )
        }
    }
}


@Composable
private fun QuestionCard(
    question: Question,
    selectedAnswer: String?,
    showResult: Boolean,
    onAnswerSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "❓ ${question.text}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )

            question.choices.forEach { choice ->
                SelectableOption(
                    text = choice,
                    isSelected = selectedAnswer == choice,
                    isCorrect = showResult && choice == question.answer,
                    showResult = showResult,
                    onClick = { onAnswerSelected(choice) }
                )
            }
        }
    }
}

@Composable
private fun SelectableOption(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    showResult: Boolean,
    onClick: () -> Unit
) {
    val defaultSurface = MaterialTheme.colorScheme.onSurface

    val backgroundColor = when {
        showResult && isSelected && isCorrect -> CorrectOption
        showResult && isSelected && !isCorrect -> WrongOption
        showResult && !isSelected && isCorrect -> CorrectOption
        !showResult && isSelected -> SelectedOption
        else -> defaultSurface
    }

    val textColor = if (backgroundColor == defaultSurface) Color.Black else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(enabled = !showResult) {
                onClick()
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text, style = MaterialTheme.typography.bodyLarge, color = textColor)
        }
    }
}

@Composable
private fun QuestionProgress(
    currentQuestionIndex: Int,
    totalQuestions: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${currentQuestionIndex + 1} / $totalQuestions",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(bottom = 12.dp, top = 8.dp)
        )

        LinearProgressIndicator(
            progress = { (currentQuestionIndex + 1).toFloat() / totalQuestions.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = Blue,
            strokeCap = StrokeCap.Round
        )
    }
}

@Composable
private fun ProgressQuizButton(
    showResult: Boolean,
    currentQuestionIndex: Int,
    questionsSize: Int,
    selectedAnswer: String?,
    onNextQuestion: () -> Unit,
    onTestComplete: () -> Unit
) {
    Button(
        onClick = {
            if (showResult) {
                if (currentQuestionIndex < questionsSize - 1) {
                    onNextQuestion()
                } else {
                    onTestComplete()
                }
            } else {
                onNextQuestion()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        enabled = selectedAnswer != null,
        colors = ButtonDefaults.buttonColors(containerColor = Blue, disabledContainerColor = Color.Gray)
    ) {
        Text(
            when {
                !showResult -> stringResource(R.string.button_check_answer)
                currentQuestionIndex == questionsSize - 1 -> stringResource(R.string.button_show_results)
                else -> stringResource(R.string.button_next_question)
            },
            color = Color.White
        )
    }
}

@Composable
fun LoadingQuizScreen(
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(stringResource(R.string.creating_quiz), style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onPrimary)
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun CompletedScreen(
    correctAnswers: Int,
    questionsSize: Int,
    onSaveQuiz: () -> Unit,
    paddingValues: PaddingValues
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = stringResource(R.string.quiz_completed),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = stringResource(R.string.result_correct_answers, correctAnswers, questionsSize),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(8.dp)
            )

            Button(
                onClick = { onSaveQuiz() },
                colors = ButtonDefaults.buttonColors(containerColor = Blue)
            ) {
                Text(stringResource(R.string.button_save_quiz), color = Color.White)
            }
        }
    }
}

@Composable
fun ErrorQuizScreen(
    paddingValues: PaddingValues,
    errorMessage: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorMessage,
            color = Color.Red,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewQuizScreen() {
    QuizGeneratorTheme {
        val sampleQuestions = listOf(
            Question(
                text = "What is the capital of Turkey?",
                choices = listOf("Paris", "Berlin", "Istanbul", "Madrid"),
                answer = "Paris"
            ),
            Question(
                text = "What is the capital of Turkey?",
                choices = listOf("Paris", "Berlin", "Istanbul", "Madrid"),
                answer = "Paris"
            )
        )

        QuizScreen(
            paddingValues = PaddingValues(0.dp),
            questions = sampleQuestions,
            currentQuestionIndex = 0,
            testCompleted = false,
            selectedAnswer = "Paris",
            showResult = true,
            correctAnswers = 1,
            onAnswerSelected = {},
            onNextQuestion = {},
            onTestComplete = {},
            onSaveQuiz = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PreviewQuizScreenDarkTheme() {
    QuizGeneratorTheme {
        val sampleQuestions = listOf(
            Question(
                text = "What is the capital of Turkey?",
                choices = listOf("Paris", "Berlin", "Istanbul", "Madrid"),
                answer = "Paris"
            ),
            Question(
                text = "What is the capital of Turkey?",
                choices = listOf("Paris", "Berlin", "Istanbul", "Madrid"),
                answer = "Paris"
            )
        )

        QuizScreen(
            paddingValues = PaddingValues(0.dp),
            questions = sampleQuestions,
            currentQuestionIndex = 0,
            testCompleted = false,
            selectedAnswer = null,
            showResult = true,
            correctAnswers = 1,
            onAnswerSelected = {},
            onNextQuestion = {},
            onTestComplete = {},
            onSaveQuiz = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PreviewQuestionCard() {
    QuizGeneratorTheme {
        val sampleQuestion = Question(
            text = "What is 2 + 2?",
            choices = listOf("3", "4", "5", "6"),
            answer = "4"
        )

        QuestionCard(
            question = sampleQuestion,
            selectedAnswer = "4",
            showResult = true,
            onAnswerSelected = {}
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewLoadingQuizScreen() {
    QuizGeneratorTheme {
        LoadingQuizScreen(
            paddingValues = PaddingValues(0.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCompletedScreen() {
    QuizGeneratorTheme {
        CompletedScreen(
            correctAnswers = 8,
            questionsSize = 10,
            onSaveQuiz = {},
            paddingValues = PaddingValues(0.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewErrorQuizScreen() {
    QuizGeneratorTheme {
        ErrorQuizScreen(
            paddingValues = PaddingValues(0.dp),
            errorMessage = "An unexpected error occurred while creating the quiz."
        )
    }
}
