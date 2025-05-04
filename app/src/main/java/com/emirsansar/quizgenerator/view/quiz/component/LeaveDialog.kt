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
fun LeaveDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.dialog_leave_header), color = MaterialTheme.colorScheme.onBackground) },
        text = { Text(stringResource(id = R.string.dialog_leave_text), color = MaterialTheme.colorScheme.onBackground) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(id = R.string.button_confirm), color = MaterialTheme.colorScheme.onBackground)
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
private fun LeaveDialogPreview(){
    QuizGeneratorTheme {
        LeaveDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun LeaveDialogPreviewDarkTheme(){
    QuizGeneratorTheme {
        LeaveDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}