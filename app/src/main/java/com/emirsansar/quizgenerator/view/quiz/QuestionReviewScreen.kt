package com.emirsansar.quizgenerator.view.quiz

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emirsansar.quizgenerator.R
import com.emirsansar.quizgenerator.model.Question
import com.emirsansar.quizgenerator.ui.theme.QuizGeneratorTheme
import com.emirsansar.quizgenerator.view.main.screen.InfoMessageScreen
import com.emirsansar.quizgenerator.view.quiz.component.DeleteDialog
import com.emirsansar.quizgenerator.view.quiz.component.QuestionReviewCard
import com.emirsansar.quizgenerator.viewmodel.QuestionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionReviewScreen(
    modifier: Modifier = Modifier,
    question: Question?,
    questionVM: QuestionViewModel?,
    onBackPress: () -> Unit
) {
    val selectedAnswer = remember { mutableStateOf<String?>(null) }
    var showAnswers by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isQuestionRemoved by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBarQuestionReview(
                onBackPress = onBackPress,
                onShowAnswersPress = { showAnswers = !showAnswers },
                showAnswers = showAnswers,
                onDeletePress = { showDeleteDialog = true },
                isQuestionRemoved = isQuestionRemoved,
                onFavouritePress = {
                    questionVM!!.insertQuestion(question!!)
                    isQuestionRemoved = false
                    Toast.makeText(context, context.getString(R.string.question_added_successfully), Toast.LENGTH_SHORT).show()
                }
            )
        },
        content = { paddingValues ->
            if (question == null) {
                InfoMessageScreen(stringResource(id = R.string.error_question_data_missing))
            } else {
                QuestionReviewContent(
                    paddingValues = paddingValues,
                    question = question,
                    selectedAnswer = selectedAnswer.value,
                    showAnswers = showAnswers,
                    onAnswerSelected = { selectedAnswer.value = it }
                )
            }
        }
    )

    if (showDeleteDialog) {
        DeleteDialog(
            itemType = "Question",
            onConfirm = {
                questionVM!!.deleteQuestion(question!!)
                showDeleteDialog = false
                isQuestionRemoved = true
                Toast.makeText(context, context.getString(R.string.question_removed_successfully), Toast.LENGTH_SHORT).show()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
fun QuestionReviewContent(
    paddingValues: PaddingValues,
    question: Question,
    selectedAnswer: String?,
    showAnswers: Boolean,
    onAnswerSelected: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        QuestionReviewCard(
            question = question,
            showAnswers = showAnswers,
            selectedAnswer = selectedAnswer,
            onAnswerSelected = onAnswerSelected
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarQuestionReview(
    onBackPress: () -> Unit,
    onShowAnswersPress: () -> Unit,
    onDeletePress: () -> Unit,
    showAnswers: Boolean,
    isQuestionRemoved: Boolean,
    onFavouritePress: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(stringResource(id = R.string.question_review_title))
        },
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button_description),
                )
            }
        },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu",
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.tertiary)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = if (!showAnswers) stringResource(id = R.string.menu_show_answer)
                                else stringResource(id = R.string.menu_hide_answer),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    onClick = {
                        onShowAnswersPress()
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = if (!isQuestionRemoved) stringResource(id = R.string.menu_delete_question)
                                else stringResource(id = R.string.menu_add_question),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    onClick = {
                        if (!isQuestionRemoved) {
                            onDeletePress()
                        } else {
                            onFavouritePress()
                        }
                        expanded = false
                    }
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


@Preview(showBackground = true)
@Composable
fun PreviewQuestionReviewScreen() {
    val sampleQuestion = Question(
        text = "What is the purpose of the `remember` function in Jetpack Compose?",
        choices = listOf(
            "To define a new layout.",
            "To cache the result of a calculation across recompositions.",
            "To trigger a recomposition.",
            "To create a new Activity."
        ),
        answer = "To cache the result of a calculation across recompositions."
    )


    QuizGeneratorTheme {
        QuestionReviewScreen(
            question = sampleQuestion,
            questionVM = null,
            onBackPress = {}
        )
    }
}
