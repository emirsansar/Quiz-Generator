package com.emirsansar.quizgenerator.view.quiz.component

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.emirsansar.quizgenerator.R
import com.emirsansar.quizgenerator.ui.theme.QuizGeneratorTheme

@Composable
fun DeleteDialog(
    itemType: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.dialog_delete_header, itemType), color = MaterialTheme.colorScheme.onBackground) },
        text = {
            Text(stringResource(id = R.string.dialog_delete_text, itemType), color = MaterialTheme.colorScheme.onBackground)
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(id = R.string.button_delete), color = MaterialTheme.colorScheme.onBackground)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.button_cancel), color = MaterialTheme.colorScheme.onBackground)
            }
        },
        containerColor = MaterialTheme.colorScheme.tertiaryContainer
    )
}


@Preview(showBackground = true)
@Composable
private fun DeleteDialogPreview(){
    QuizGeneratorTheme {
        DeleteDialog(
            itemType = "Quiz",
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun DeleteDialogPreviewDarkTheme(){
    QuizGeneratorTheme {
        DeleteDialog(
            itemType = "Question",
            onConfirm = {},
            onDismiss = {}
        )
    }
}