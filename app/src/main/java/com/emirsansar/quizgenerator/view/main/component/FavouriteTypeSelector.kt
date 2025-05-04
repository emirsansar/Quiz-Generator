package com.emirsansar.quizgenerator.view.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emirsansar.quizgenerator.R
import com.emirsansar.quizgenerator.ui.theme.QuizGeneratorTheme
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteTypeSelector(
    options: List<FavouriteType>,
    selectedFilterType: FavouriteType,
    onOptionSelected: (FavouriteType) -> Unit,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedFilterType.getLocalizedName(),
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                label = { Text(stringResource(R.string.favourite_type_label), color = MaterialTheme.colorScheme.onSecondary) },
                trailingIcon = {
                    val icon = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(
                            if (expanded) R.string.dropdown_collapse else R.string.dropdown_expand
                        ),
                        modifier = Modifier.clickable { expanded = !expanded },
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    focusedBorderColor = MaterialTheme.colorScheme.onTertiary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSecondary,
                    disabledBorderColor = MaterialTheme.colorScheme.onSecondary,
                    errorBorderColor = Color.Red,
                    focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                    disabledLabelColor = MaterialTheme.colorScheme.onSecondary,
                    errorLabelColor = Color.Red
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.tertiary)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.getLocalizedName(), color = MaterialTheme.colorScheme.onSecondary) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


enum class FavouriteType {
    Quizzes,
    Questions;

    @Composable
    fun getLocalizedName(): String {
        return when (this) {
            Quizzes -> stringResource(R.string.favourite_type_quizzes)
            Questions -> stringResource(R.string.favourite_type_questions)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun FavouriteTypeSelectorPreview() {
    val selectedType = remember { mutableStateOf(FavouriteType.Quizzes) }

    QuizGeneratorTheme {
        FavouriteTypeSelector(
            options = listOf(FavouriteType.Quizzes, FavouriteType.Questions),
            selectedFilterType = selectedType.value,
            onOptionSelected = { selectedType.value = it },
            modifier = Modifier
        )
    }
}
