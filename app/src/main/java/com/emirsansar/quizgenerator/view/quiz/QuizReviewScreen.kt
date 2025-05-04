package com.emirsansar.quizgenerator.view.quiz

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.emirsansar.quizgenerator.R
import com.emirsansar.quizgenerator.model.Question
import com.emirsansar.quizgenerator.model.Quiz
import com.emirsansar.quizgenerator.view.quiz.component.DeleteDialog
import com.emirsansar.quizgenerator.view.quiz.component.QuestionReviewCard
import com.emirsansar.quizgenerator.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizReviewScreen(
    modifier: Modifier = Modifier,
    quiz: Quiz,
    quizVM: QuizViewModel,
    onBackPress: () -> Unit
)
{
    var showAnswers by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isQuizRemoved by remember { mutableStateOf(false) }
    var isExpandedAllQuestions by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBarQuizReview(
                onBackPress = onBackPress,
                onShowAnswersPress = { showAnswers = !showAnswers },
                showAnswers = showAnswers,
                onDeletePress = { showDeleteDialog = true },
                isQuizRemoved = isQuizRemoved,
                onFavouritePress = {
                    quizVM.insertQuiz(quiz)
                    isQuizRemoved = false
                    Toast.makeText(context, context.getString(R.string.quiz_added_successfully), Toast.LENGTH_SHORT).show()
                },
                isExpandedAllQuestions = isExpandedAllQuestions,
                onExpandPress = {
                    isExpandedAllQuestions = !isExpandedAllQuestions
                }
            )
        },
        content = { paddingValues ->
            QuizReviewContent(
                paddingValues = paddingValues,
                quiz = quiz,
                showAnswers = showAnswers,
                isExpandedAll = isExpandedAllQuestions
            )
        }
    )

    if (showDeleteDialog) {
        DeleteDialog(
            itemType = "Quiz",
            onConfirm = {
                quizVM.deleteQuiz(quiz)
                showDeleteDialog = false
                isQuizRemoved = true
                Toast.makeText(context, context.getString(R.string.quiz_removed_successfully), Toast.LENGTH_SHORT).show()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
fun QuizReviewContent(
    paddingValues: PaddingValues,
    quiz: Quiz,
    showAnswers: Boolean,
    isExpandedAll: Boolean
) {
    // Stores the selected answer for each question by index.
    val selectedAnswers = remember { mutableStateMapOf<Int, String?>() }
    // Keeps track of which questions are currently expanded (visible).
    val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

    LaunchedEffect(isExpandedAll) {
        quiz.questions.indices.forEach { index ->
            expandedStates[index] = isExpandedAll
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.tertiary)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(quiz.questions.size) { index ->
                val question = quiz.questions[index]
                val isExpanded = expandedStates[index] == true

                ExpandableQuestionCard(
                    index = index,
                    question = question,
                    isExpanded = isExpanded,
                    selectedAnswer = selectedAnswers[index],
                    showAnswers = showAnswers,
                    onToggleExpand = {
                        expandedStates[index] = !(expandedStates[index] ?: false)
                    },
                    onAnswerSelected = { answer ->
                        selectedAnswers[index] = answer
                    }
                )
            }
        }
    }
}

@Composable
private fun ExpandableQuestionCard(
    index: Int,
    question: Question,
    isExpanded: Boolean,
    selectedAnswer: String?,
    showAnswers: Boolean,
    onToggleExpand: () -> Unit,
    onAnswerSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onToggleExpand() // Toggle expand/collapse state when card is clicked.
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                ,
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.place_of_question, index+1),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) stringResource(R.string.dropdown_collapse) else stringResource(R.string.dropdown_expand),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Show the question content only when expanded.
            if (isExpanded) {
                Spacer(modifier = Modifier.height(5.dp))

                QuestionReviewCard(
                    question = question,
                    showAnswers = showAnswers,
                    selectedAnswer = selectedAnswer,
                    onAnswerSelected = onAnswerSelected
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarQuizReview(
    onBackPress: () -> Unit,
    onShowAnswersPress: () -> Unit,
    onDeletePress: () -> Unit,
    showAnswers: Boolean,
    isQuizRemoved: Boolean,
    onFavouritePress: () -> Unit,
    isExpandedAllQuestions: Boolean,
    onExpandPress: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Text(stringResource(id = R.string.quiz_review_title))
        },
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button_description)
                )
            }
        },
        actions = {
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Menu"
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
                            text = if (!showAnswers) stringResource(id = R.string.menu_show_answers)
                                else stringResource(id = R.string.menu_hide_answers),
                            color = MaterialTheme.colorScheme.onPrimary)
                    },
                    onClick = {
                        onShowAnswersPress()
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            text = if (!isExpandedAllQuestions) stringResource(id = R.string.expand_questions)
                            else stringResource(id = R.string.dropdown_collapse),
                            color = MaterialTheme.colorScheme.onPrimary)
                    },
                    onClick = {
                        onExpandPress()
                        expanded = false
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            text = if (!isQuizRemoved) stringResource(id = R.string.menu_delete_quiz)
                                else stringResource(id = R.string.menu_add_quiz),
                            color = MaterialTheme.colorScheme.onPrimary)
                    },
                    onClick = {
                        if (!isQuizRemoved) {
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