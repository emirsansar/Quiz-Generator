package com.emirsansar.quizgenerator.view.main.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.emirsansar.quizgenerator.R
import com.emirsansar.quizgenerator.ui.theme.QuizGeneratorTheme

@Composable
fun DeleteQuizzesDialog(
    onDeleteConfirmed: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(id = R.string.header_quizzes_for_delete_dialog), fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground) },
        text = {
            Text(stringResource(id = R.string.text_dialog_all_quizzes), color = MaterialTheme.colorScheme.onBackground)
        },
        confirmButton = {
            TextButton(onClick = {
                onDeleteConfirmed()
                onDismiss()
            }) {
                Text(stringResource(id = R.string.button_delete), color = MaterialTheme.colorScheme.onBackground)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(id = R.string.button_cancel), color = MaterialTheme.colorScheme.onBackground)
            }
        },
        containerColor = MaterialTheme.colorScheme.tertiaryContainer
    )
}


@Composable
@Preview(showBackground = true)
private fun DeleteQuizzesDialogPreview() {
    QuizGeneratorTheme {
        DeleteQuizzesDialog(
            onDeleteConfirmed = {},
            onDismiss = {}
        )
    }
}
