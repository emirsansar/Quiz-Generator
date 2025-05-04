package com.emirsansar.quizgenerator.view.quiz.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emirsansar.quizgenerator.R
import com.emirsansar.quizgenerator.model.Question
import com.emirsansar.quizgenerator.ui.theme.QuizGeneratorTheme

@Composable
fun QuestionReviewCard(
    question: Question,
    showAnswers: Boolean,
    selectedAnswer: String?,
    onAnswerSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "\u2753 ${question.text}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            question.choices.forEach { choice ->
                AnswerOption(
                    text = choice,
                    isCorrect = choice == question.answer,
                    isSelected = choice == selectedAnswer,
                    showAnswers = showAnswers,
                    onSelected = onAnswerSelected
                )
            }

            if (showAnswers) {
                AnswerResultText(selectedAnswer, question.answer)
            }
        }
    }
}

@Composable
private fun AnswerOption(
    text: String,
    isCorrect: Boolean,
    isSelected: Boolean,
    showAnswers: Boolean,
    onSelected: (String) -> Unit
) {
    val successColor = Color(0xFF4CAF50)
    val errorColor = Color(0xFFFF5252)
    val selectedColor = Color(0xFF6D92EF)
    val defaultSurface = MaterialTheme.colorScheme.onSurface

    val backgroundColor = when {
        showAnswers && isSelected && isCorrect -> successColor
        showAnswers && isSelected && !isCorrect -> errorColor
        showAnswers && !isSelected && isCorrect -> successColor
        !showAnswers && isSelected -> selectedColor
        else -> defaultSurface
    }

    val textColor = if (backgroundColor == defaultSurface) Color.Black else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(enabled = !showAnswers) { onSelected(text) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text,
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
        }
    }
}

@Composable
private fun AnswerResultText(
    selectedAnswer: String?,
    correctAnswer: String
) {
    val isCorrect = selectedAnswer == correctAnswer
    val resultText = when {
        selectedAnswer == null -> stringResource(id = R.string.text_no_choice)
        isCorrect -> stringResource(id = R.string.text_correct_answer)
        else -> stringResource(id = R.string.text_wrong_answer)
    }

    val resultColor = when {
        selectedAnswer == null -> MaterialTheme.colorScheme.onTertiary
        isCorrect -> Color(0xFF4CAF50)
        else -> Color(0xFFFF5252)
    }

    Text(
        text = resultText,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 12.dp, bottom = 4.dp),
        color = resultColor
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewQuestionReviewCard() {
    val sampleQuestion = Question(
        text = "What is the capital of France?",
        choices = listOf("Paris", "London", "Berlin", "Rome"),
        answer = "Paris"
    )

    QuizGeneratorTheme {
        QuestionReviewCard(
            question = sampleQuestion,
            showAnswers = true,
            selectedAnswer = null,
            onAnswerSelected = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PreviewQuestionReviewCardDarkTheme() {
    val sampleQuestion = Question(
        text = "What is the capital of France?",
        choices = listOf("Paris", "London", "Berlin", "Rome"),
        answer = "Paris"
    )

    QuizGeneratorTheme {
        QuestionReviewCard(
            question = sampleQuestion,
            showAnswers = true,
            selectedAnswer = null,
            onAnswerSelected = {}
        )
    }
}
