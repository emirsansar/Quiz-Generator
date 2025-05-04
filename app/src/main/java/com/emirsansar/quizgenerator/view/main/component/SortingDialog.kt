package com.emirsansar.quizgenerator.view.main.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emirsansar.quizgenerator.R
import com.emirsansar.quizgenerator.ui.theme.QuizGeneratorTheme

@Composable
fun SortingDialog(
    currentSorting: SortingOption,
    onSortingConfirmed: (SortingOption) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedSortingTemp by remember { mutableStateOf(currentSorting) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(stringResource(id = R.string.sorting_option_header), fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground) },
        text = {
            Column {
                SortingOption.entries.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedSortingTemp = option }
                            .padding(8.dp)
                    ) {
                        RadioButton(
                            selected = (option == selectedSortingTemp),
                            onClick = { selectedSortingTemp = option },
                            colors = RadioButtonDefaults.colors(selectedColor = Color.Blue)
                        )

                        Text(
                            text = option.getDisplayName(),
                            modifier = Modifier.padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSortingConfirmed(selectedSortingTemp)
                onDismiss()
            }) {
                Text(stringResource(id = R.string.button_confirm), color = MaterialTheme.colorScheme.onBackground)
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

enum class SortingOption {
    Newest,
    Oldest;

    @Composable
    fun getDisplayName(): String {
        return when (this) {
            Newest -> stringResource(R.string.sort_newest)
            Oldest -> stringResource(R.string.sort_oldest)
        }
    }
}



@Preview(showBackground = true)
@Composable
private fun SortingDialogPreview(){
    QuizGeneratorTheme {
        SortingDialog(
            currentSorting = SortingOption.Newest,
            onSortingConfirmed = {},
            onDismiss = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun SortingDialogPreviewDarkTheme(){
    QuizGeneratorTheme {
        SortingDialog(
            currentSorting = SortingOption.Newest,
            onSortingConfirmed = {},
            onDismiss = {}
        )
    }
}