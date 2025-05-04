package com.emirsansar.quizgenerator.view.main.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emirsansar.quizgenerator.AppSettings
import com.emirsansar.quizgenerator.R
import com.emirsansar.quizgenerator.ui.theme.QuizGeneratorTheme
import com.emirsansar.quizgenerator.view.main.component.DeleteQuestionsDialog
import com.emirsansar.quizgenerator.view.main.component.DeleteQuizzesDialog
import com.emirsansar.quizgenerator.viewmodel.QuestionViewModel
import com.emirsansar.quizgenerator.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier,
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    quizViewModel: QuizViewModel?,
    questionViewModel: QuestionViewModel?
) {
    var isVisibleDeleteQuizzesDialog by remember { mutableStateOf(false) }
    var isVisibleDeleteQuestionsDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        SettingsLabel()

        LanguageDropDown(
            selectedLanguage = selectedLanguage,
            onLanguageChange = onLanguageChange
        )

        DarkThemeToggle(
            isDarkTheme = isDarkTheme,
            onThemeToggle = onThemeToggle
        )

        Spacer(modifier = Modifier.weight(1f))

        Column {
            DeleteAllQuizzesButton(onClick = { isVisibleDeleteQuizzesDialog = true})
            DeleteAllQuestionsButton(onClick = { isVisibleDeleteQuestionsDialog = true} )
        }
    }

    if (isVisibleDeleteQuizzesDialog) {
        DeleteQuizzesDialog(
            onDeleteConfirmed = {
                quizViewModel?.deleteAllQuizzes()

                Toast.makeText(
                    context,
                    context.getString(R.string.all_quizzes_deleted),
                    Toast.LENGTH_SHORT
                ).show()
                AppSettings.isDeletedQuizzes = true
            },
            onDismiss = { isVisibleDeleteQuizzesDialog = false}
        )
    }

    if (isVisibleDeleteQuestionsDialog) {
        DeleteQuestionsDialog(
            onDeleteConfirmed = {
                questionViewModel?.deleteAllQuestions()

                Toast.makeText(
                    context,
                    context.getString(R.string.all_questions_deleted),
                    Toast.LENGTH_SHORT
                ).show()
                AppSettings.isDeletedQuestions = true
            },
            onDismiss = { isVisibleDeleteQuestionsDialog = false}
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageDropDown(
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit
){
    val languages = listOf("English", "Türkçe")
    var isDropdownExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isDropdownExpanded,
        onExpandedChange = { isDropdownExpanded = !isDropdownExpanded },
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        OutlinedTextField(
            value = selectedLanguage,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.label_language)) },
            trailingIcon = {
                Icon(
                    imageVector = if (isDropdownExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onSecondary,
                focusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                errorBorderColor = Color.Red,
                errorLabelColor = Color.Red
            )
        )

        ExposedDropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.tertiary)
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language, color = MaterialTheme.colorScheme.onSecondary )},
                    onClick = {
                        onLanguageChange(language)
                        isDropdownExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DarkThemeToggle(
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(stringResource(id = R.string.label_dark_mode),
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(start = 4.dp)
        )

        Switch(
            checked = isDarkTheme,
            onCheckedChange = onThemeToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Green
            ),
            modifier = Modifier.padding(end = 2.dp)
        )
    }
}

@Composable
private fun DeleteAllQuizzesButton(
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = {
            onClick()
        },
        modifier = Modifier.fillMaxWidth().padding(horizontal = 26.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Red,
            containerColor = Color.Transparent
        ),
    ) {
        Text(text = stringResource(id = R.string.button_delete_all_quizzes))
    }
}

@Composable
private fun DeleteAllQuestionsButton(
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = {
            onClick()
        },
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, start = 26.dp, end = 26.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.Red,
            containerColor = Color.Transparent
        )
    ) {
        Text(text = stringResource(id = R.string.button_delete_all_questions))
    }
}

@Composable
private fun SettingsLabel() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(top = 24.dp, bottom = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.settings_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        HorizontalDivider(
            modifier = Modifier.height(1.dp).padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.onTertiary
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    var selectedLang by remember { mutableStateOf("English") }
    var isDarkTheme by remember { mutableStateOf(false) }

    QuizGeneratorTheme {
        SettingsScreen(
            modifier = Modifier,
            selectedLanguage = selectedLang,
            onLanguageChange = { selectedLang = it },
            isDarkTheme = isDarkTheme,
            onThemeToggle = { isDarkTheme = it },
            questionViewModel = null,
            quizViewModel = null
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SettingsScreenPreviewDark() {
    var selectedLang by remember { mutableStateOf("Türkçe") }
    var isDarkTheme by remember { mutableStateOf(true) }

    QuizGeneratorTheme {
        SettingsScreen(
            modifier = Modifier,
            selectedLanguage = selectedLang,
            onLanguageChange = { selectedLang = it },
            isDarkTheme = isDarkTheme,
            onThemeToggle = { isDarkTheme = it },
            questionViewModel = null,
            quizViewModel = null
        )
    }
}