package com.emirsansar.quizgenerator.view.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.emirsansar.quizgenerator.ui.theme.QuizGeneratorTheme
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.emirsansar.quizgenerator.AppSettings
import com.emirsansar.quizgenerator.R
import com.emirsansar.quizgenerator.model.Question
import com.emirsansar.quizgenerator.model.Quiz
import com.emirsansar.quizgenerator.view.main.screen.FavouriteScreen
import com.emirsansar.quizgenerator.view.main.screen.HomeScreen
import com.emirsansar.quizgenerator.view.main.screen.SettingsScreen
import com.emirsansar.quizgenerator.view.quiz.QuestionReviewScreen
import com.emirsansar.quizgenerator.view.quiz.QuizReviewScreen
import com.emirsansar.quizgenerator.viewmodel.QuestionViewModel
import com.emirsansar.quizgenerator.viewmodel.QuizViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppSettings.initialize(this)

        enableEdgeToEdge()

        setContent {
            QuizGeneratorTheme(darkTheme = AppSettings.isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainActivityScreen()
                }
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, @StringRes val titleRes: Int) {
    object Settings : BottomNavItem("settings_screen", Icons.Default.Settings, R.string.nav_settings)
    object Home : BottomNavItem("home_screen", Icons.Default.Home, R.string.nav_home)
    object Saved : BottomNavItem("favourite_screen", Icons.Default.Favorite, R.string.nav_saved)
}

@Composable
fun MainActivityScreen() {
    val items = listOf(BottomNavItem.Settings, BottomNavItem.Home, BottomNavItem.Saved)
    val selectedBar = remember { mutableIntStateOf(1) }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    val quizViewModel: QuizViewModel = hiltViewModel()
    val questionViewModel: QuestionViewModel = hiltViewModel()

    var selectedFavQuiz by remember { mutableStateOf<Quiz?>(null) }
    var selectedFavQuestion by remember { mutableStateOf<Question?>(null) }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Scaffold(
            content = { paddingValues ->
                NavHost(navController = navController, startDestination = "home_screen") {
                    composable("home_screen") {
                        HomeScreen(modifier = Modifier.padding(paddingValues))
                    }

                    composable("settings_screen") {
                        SettingsScreen(
                            modifier = Modifier.padding(paddingValues),
                            selectedLanguage = AppSettings.selectedLanguage,
                            onLanguageChange = {
                                AppSettings.changeLanguage(context, it)

                                //(context as? Activity)?.recreate()

                                navController.navigate("settings_screen") {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = false
                                    }
                                    launchSingleTop = true
                                }
                            },
                            isDarkTheme = AppSettings.isDarkTheme,
                            onThemeToggle = { AppSettings.changeTheme(context, it) },
                            questionViewModel = questionViewModel,
                            quizViewModel = quizViewModel
                        )
                    }


                    composable("favourite_screen") {
                        FavouriteScreen(
                            modifier = Modifier.padding(paddingValues),
                            questionViewModel = questionViewModel,
                            quizViewModel = quizViewModel,
                            navController = navController,
                            onQuizSelected = { selectedFavQuiz = it },
                            onQuestionSelected = { selectedFavQuestion = it }
                        )
                    }

                    composable("quiz_review_screen") { backStackEntry ->
                        if (selectedFavQuiz != null) {
                            QuizReviewScreen(
                                modifier = Modifier.padding(paddingValues),
                                quiz = selectedFavQuiz!!,
                                quizVM = quizViewModel,
                                onBackPress = { navController.popBackStack() }
                            )
                        }
                    }

                    composable("question_review_screen") { backStackEntry ->
                        if (selectedFavQuestion != null) {
                            QuestionReviewScreen(
                                modifier = Modifier.padding(paddingValues),
                                question = selectedFavQuestion!!,
                                questionVM = questionViewModel,
                                onBackPress = { navController.popBackStack() }
                            )
                        }
                    }
                }
            },
            bottomBar = {
                if (currentRoute != "quiz_review_screen" && currentRoute != "question_review_screen") {
                    ApplicationNavigationBar(
                        items = items,
                        selectedRoute = currentRoute,
                        onItemSelected = { item ->
                            selectedBar.intValue = items.indexOf(item)
                            navigateAndClearBackStack(navController, item.route)
                        }
                    )

                }
            }
        )
    }
}

@Composable
fun ApplicationNavigationBar(
    items: List<BottomNavItem>,
    selectedRoute: String?,
    onItemSelected: (BottomNavItem) -> Unit
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            NavigationBarItem(
                item = item,
                isSelected = item.route == selectedRoute,
                onClick = { onItemSelected(item) }
            )
        }
    }
}

@Composable
fun NavigationBarItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val background = if (isSelected)
        MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.4f)
    else
        Color.Transparent

    val contentColor = if (isSelected)
        MaterialTheme.colorScheme.onBackground
    else
        MaterialTheme.colorScheme.onTertiary

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = stringResource(id = item.titleRes),
                tint = contentColor
            )
            AnimatedVisibility(visible = isSelected) {
                Text(
                    text = stringResource(id = item.titleRes),
                    color = contentColor
                )
            }
        }
    }
}

// Navigate to a new destination while clearing all previous screens from the back stack.
private fun navigateAndClearBackStack(
    navController: NavController,
    destination: String
) {
    navController.navigate(destination) {
        popUpTo(navController.graph.startDestinationId) { inclusive = false }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewMainActivity() {
    QuizGeneratorTheme {
        MainActivityScreen()
    }
}