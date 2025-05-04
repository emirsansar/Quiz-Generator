package com.emirsansar.quizgenerator.view.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.emirsansar.quizgenerator.model.Quiz
import com.emirsansar.quizgenerator.viewmodel.QuizViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.emirsansar.quizgenerator.AppSettings
import com.emirsansar.quizgenerator.R
import com.emirsansar.quizgenerator.model.Question
import com.emirsansar.quizgenerator.ui.theme.QuizGeneratorTheme
import com.emirsansar.quizgenerator.view.main.component.FavouriteType
import com.emirsansar.quizgenerator.view.main.component.FavouriteTypeSelector
import com.emirsansar.quizgenerator.view.main.component.SortingDialog
import com.emirsansar.quizgenerator.view.main.component.SortingOption
import com.emirsansar.quizgenerator.viewmodel.QuestionViewModel

@Composable
fun FavouriteScreen(
    modifier: Modifier,
    quizViewModel: QuizViewModel,
    questionViewModel: QuestionViewModel,
    navController: NavController,
    onQuizSelected: (Quiz) -> Unit,
    onQuestionSelected: (Question) -> Unit
) {
    val quizList by quizViewModel.quizzes.observeAsState(initial = emptyList())
    val questionList by questionViewModel.questions.observeAsState(initial = emptyList())

    var selectedFilterType by rememberSaveable { mutableStateOf<FavouriteType>(FavouriteType.Quizzes) }
    var isSortingDialogVisible by remember { mutableStateOf(false) }
    var sortingOption by remember { mutableStateOf(SortingOption.Newest) }

    LaunchedEffect(AppSettings.isDeletedQuizzes, AppSettings.isDeletedQuestions, AppSettings.isSavedCreatedQuiz) {
        if (AppSettings.isDeletedQuizzes) {
            quizViewModel.fetchQuizzes()
            AppSettings.isDeletedQuizzes = false
        }

        if (AppSettings.isDeletedQuestions) {
            questionViewModel.fetchQuestions()
            AppSettings.isDeletedQuestions = false
        }

        if (AppSettings.isSavedCreatedQuiz) {
            quizViewModel.fetchQuizzes()
            questionViewModel.fetchQuestions()
            AppSettings.isSavedCreatedQuiz = false
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FavouriteTypeSelector(
                options = listOf(FavouriteType.Quizzes, FavouriteType.Questions),
                selectedFilterType = selectedFilterType,
                onOptionSelected = { selectedFilterType = it },
                modifier = Modifier.weight(0.85f)
            )

            FilterButton(
                onClick = { isSortingDialogVisible = true },
                modifier = Modifier.weight(0.15f)
            )
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Black)

        Box (modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)) {
            when (selectedFilterType) {
                FavouriteType.Quizzes ->
                    FavouriteQuizzesContent(
                        quizList = quizList,
                        onSelectedFavQuiz = onQuizSelected,
                        navController = navController
                    )
                FavouriteType.Questions ->
                    FavouriteQuestionsContent(
                        questionList = questionList,
                        onSelectedFavQuestion = onQuestionSelected,
                        navController = navController
                    )
            }
        }
    }

    if (isSortingDialogVisible) {
        SortingDialog(
            currentSorting = sortingOption,
            onSortingConfirmed = { newSorting ->
                sortingOption = newSorting
                quizViewModel.sortQuizzesByDate(newSorting)
                questionViewModel.sortQuestionsByDate(newSorting)
            },
            onDismiss = { isSortingDialogVisible = false }
        )
    }
}

@Composable
private fun FavouriteQuizzesContent(
    quizList: List<Quiz>,
    navController: NavController,
    onSelectedFavQuiz: (Quiz) -> Unit
) {
    if (quizList.isEmpty()) {
        InfoMessageScreen(stringResource(R.string.text_no_saved_quizzes))
    } else {
        QuizList(quizList = quizList, navController = navController, onSelectedFavQuiz = onSelectedFavQuiz)
    }
}

@Composable
private fun FavouriteQuestionsContent(
    questionList: List<Question>,
    navController: NavController,
    onSelectedFavQuestion: (Question) -> Unit
) {
    if (questionList.isEmpty()) {
        InfoMessageScreen(stringResource(R.string.text_no_saved_questions))
    } else {
        QuestionList(questionList = questionList, onSelectedFavQuestion = onSelectedFavQuestion, navController = navController)
    }
}

@Composable
fun InfoMessageScreen(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
private fun QuizList(
    quizList: List<Quiz>,
    navController: NavController,
    onSelectedFavQuiz: (Quiz) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(quizList) { quiz ->
            QuizItem(
                quiz = quiz,
                navController = navController,
                onSelectedFavQuiz = onSelectedFavQuiz
            )
        }
    }
}

@Composable
private fun QuizItem(
    quiz: Quiz,
    navController: NavController,
    onSelectedFavQuiz: (Quiz) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
            .clickable {
                onSelectedFavQuiz(quiz)
                navController.navigate("quiz_review_screen")
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Text(
            text = "\uD83D\uDD24 " + quiz.topic,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp),
            color = Color.Black
        )
    }
}

@Composable
private fun QuestionList(
    questionList: List<Question>,
    navController: NavController,
    onSelectedFavQuestion: (Question) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(questionList) { question ->
            QuestionItem(
                question = question,
                navController = navController,
                onSelectedFavQuestion = onSelectedFavQuestion
            )
        }
    }
}

@Composable
private fun QuestionItem(
    question: Question,
    navController: NavController,
    onSelectedFavQuestion: (Question) -> Unit
) {
    val truncatedText = if (question.text.length > 50) {
        "\u2753 " + question.text.take(90) + "..."
    } else {
        question.text
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
            .clickable {
                onSelectedFavQuestion(question)
                navController.navigate("question_review_screen")
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer)
    ) {
        Text(
            text = truncatedText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp),
            color = Color.Black
        )
    }
}

@Composable
private fun FilterButton(
    onClick: () -> Unit,
    modifier: Modifier
) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier
            .padding(end = 16.dp, top = 6.dp)
            .height(56.dp)
            .border(1.dp, MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(6.dp))
            .clip(RoundedCornerShape(6.dp))
    ) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = "Filter",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewFavouriteQuizzesContent() {
    QuizGeneratorTheme {
        FavouriteQuizzesContent(
            quizList = listOf(
                Quiz(topic = "Kotlin Basics", questions = listOf()),
                Quiz(topic = "Jetpack Compose", questions = listOf())
            ),
            navController = rememberNavController(),
            onSelectedFavQuiz = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFavouriteQuestionsContent() {
    QuizGeneratorTheme {
        FavouriteQuestionsContent(
            questionList = listOf(
                Question(
                    text = "What is Jetpack Compose?",
                    choices = listOf("A UI toolkit", "A game", "An IDE", "A database"),
                    answer = "A UI toolkit"
                ),
                Question(
                    text = "Which function is used to remember a value?",
                    choices = listOf("remember()", "save()", "store()", "cache()"),
                    answer = "remember()"
                )
            ),
            navController = rememberNavController(),
            onSelectedFavQuestion = {}
        )
    }
}