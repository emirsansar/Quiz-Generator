package com.emirsansar.quizgenerator.view.main.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emirsansar.quizgenerator.R
import com.emirsansar.quizgenerator.ui.theme.QuizGeneratorTheme
import com.emirsansar.quizgenerator.view.main.component.LanguageSelector
import com.emirsansar.quizgenerator.view.main.component.QuizGeneratorLabel
import com.emirsansar.quizgenerator.view.quiz.QuizActivity
import com.emirsansar.quizgenerator.view.main.component.Selector

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    var topic by remember { mutableStateOf("") }

    val defaultDifficulty = stringResource(R.string.difficulty_medium)
    val defaultQuizType = stringResource(R.string.quiz_type_multiple_choice)
    val defaultLanguage = stringResource(R.string.language_turkish)
    val defaultQuestionCount = "5"

    var selectedDifficulty by remember { mutableStateOf(defaultDifficulty) }
    var selectedQuizType by remember { mutableStateOf(defaultQuizType) }
    var selectedLanguage by remember { mutableStateOf(defaultLanguage) }
    var selectedQuestionCount by remember { mutableStateOf(defaultQuestionCount) }

    val isKeyboardOpen = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val spacerWeight = if (isKeyboardOpen) 0.1f else 1f

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        QuizGeneratorLabel()

        QuizOptionsCard(
            topic = topic,
            onTopicChange = { topic = it },
            selectedDifficulty = selectedDifficulty,
            onDifficultyChange = { selectedDifficulty = it },
            selectedQuizType = selectedQuizType,
            onQuizTypeChange = { selectedQuizType = it },
            selectedLanguage = selectedLanguage,
            onLanguageChange = { selectedLanguage = it },
            selectedQuestionCount = selectedQuestionCount,
            onCountChange = { selectedQuestionCount = it }
        )

        Spacer(modifier = Modifier.weight(spacerWeight))

        NavigateToQuizButton(context, topic, selectedDifficulty, selectedQuizType, selectedLanguage, selectedQuestionCount)
    }
}


@Composable
private fun QuizOptionsCard(
    topic: String,
    onTopicChange: (String) -> Unit,
    selectedDifficulty: String,
    onDifficultyChange: (String) -> Unit,
    selectedQuizType: String,
    onQuizTypeChange: (String) -> Unit,
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit,
    selectedQuestionCount: String,
    onCountChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.5.dp,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        //horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopicInputField(topic, onTopicChange = onTopicChange)

        Column {
            HeaderSelector(stringResource(R.string.header_difficulty))
            DifficultySelector(selectedDifficulty, onDifficultyChange)
        }

        Column {
            HeaderSelector(stringResource(R.string.header_quiz_type))
            TestTypeSelector(selectedQuizType, onQuizTypeChange)
        }

        Column {
            HeaderSelector(stringResource(R.string.header_question_count))
            QuestionCountSelector(selectedQuestionCount, onCountChange)
        }

        Column {
            HeaderSelector(stringResource(R.string.header_language))
            LanguageSelector(selectedLanguage, onLanguageChange)
        }
    }
}

@Composable
private fun TopicInputField(
    topic: String,
    onTopicChange: (String) -> Unit
) {
    val isMaxLengthReached = topic.length >= 250
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        OutlinedTextField(
            value = topic,
            onValueChange = { if (it.length <= 250) onTopicChange(it) },
            label = { Text(stringResource(id = R.string.label_enter_topic), color = MaterialTheme.colorScheme.onTertiary) },
            modifier = Modifier.fillMaxWidth(),
            isError = isMaxLengthReached,
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onSecondary,
                cursorColor = MaterialTheme.colorScheme.onTertiary,
                focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(id = R.string.reset_button_description),
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .size(22.dp)
                    .padding(top = 6.dp)
                    .alpha(0.9f)
                    .clickable {
                        onTopicChange("")
                    }
            )

            Text(
                text = "${topic.length} / 250",
                fontSize = 13.sp,
                color = if (isMaxLengthReached) Color.Red else MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun DifficultySelector(
    selectedDifficulty: String,
    onSelectionChange: (String) -> Unit
) {
    Selector(
        options = listOf(
            stringResource(R.string.difficulty_easy),
            stringResource(R.string.difficulty_medium),
            stringResource(R.string.difficulty_hard)
        ),
        selectedOption = selectedDifficulty,
        onSelectionChange = onSelectionChange,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun TestTypeSelector(
    selectedQuizType: String,
    onSelectionChange: (String) -> Unit
) {
    Selector(
        options = listOf(
            stringResource(R.string.quiz_type_multiple_choice),
            stringResource(R.string.quiz_type_true_false)
        ),
        selectedOption = selectedQuizType,
        onSelectionChange = onSelectionChange,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun QuestionCountSelector(
    selectedQuestionCount: String,
    onSelectionChange: (String) -> Unit
) {
    Selector(
        options = listOf("5", "10", "15", "20"),
        selectedOption = selectedQuestionCount,
        onSelectionChange = onSelectionChange,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun NavigateToQuizButton(
    context: Context,
    topic: String,
    difficulty: String,
    testType: String,
    language: String,
    questionCount: String
) {
    Button(
        onClick = {
            val intent = Intent(context, QuizActivity::class.java).apply {
                putExtra("topic", topic)
                putExtra("difficulty", difficulty)
                putExtra("test_type", testType)
                putExtra("language", language)
                putExtra("question_count", questionCount)
            }
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF045B8C)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp,
            hoveredElevation = 10.dp
        )
    ) {
        Text(stringResource(id = R.string.button_generate_quiz), fontSize = 18.sp, color = Color.White)
    }
}

@Composable
private fun HeaderSelector(
    text: String
) {
    Text(text = text, fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSecondary,
        modifier = Modifier.alpha(0.9f) )
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    QuizGeneratorTheme {
        HomeScreen()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PreviewHomeScreenDarkTheme() {
    QuizGeneratorTheme {
        HomeScreen()
    }
}
