package com.goldenraccoon.mementomoricalendar.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.goldenraccoon.mementomoricalendar.ui.viewmodels.SettingsViewModel
import com.goldenraccoon.mementomoricalendar.ui.views.PastSelectableDates
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SettingsPage(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val initialLifeExpectancy by viewModel.lifeExpectancyYears.collectAsStateWithLifecycle()
    val initialBirthdayMillis by viewModel.initialBirthdayMillis.collectAsStateWithLifecycle()

    LaunchedEffect(initialLifeExpectancy) {
        viewModel.onLifeExpectancyInputChange(initialLifeExpectancy.toString())
    }

    SettingsPageContent(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        lifeExpectancyText = viewModel.lifeExpectancyInput,
        initialLifeExpectancy = initialLifeExpectancy,
        birthdayMillis = viewModel.birthdayMillisInput ?: initialBirthdayMillis,
        onLifeExpectancyChange = {
            viewModel.onLifeExpectancyInputChange(it)
        },
        onBirthdayChange = {
            viewModel.onBirthdayInputChange(it)
        },
        onSaveClick = {
            viewModel.saveSettings()
            onNavigateBack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPageContent(
    modifier: Modifier = Modifier,
    lifeExpectancyText: String,
    initialLifeExpectancy: Int,
    birthdayMillis: Long?,
    onLifeExpectancyChange: (String) -> Unit,
    onBirthdayChange: (Long?) -> Unit,
    onSaveClick: () -> Unit
) {
    var showDatePickerModal by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        selectableDates = PastSelectableDates
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = lifeExpectancyText,
                onValueChange = onLifeExpectancyChange,
                label = { Text("Life Expectancy") },
                placeholder = { Text(initialLifeExpectancy.toString(), color = Color.Gray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = birthdayMillis?.let { convertMillisToDate(it) } ?: "",
                readOnly = true,
                onValueChange = { },
                label = { Text("Birthday") },
                trailingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = "Calendar icon")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(birthdayMillis) {
                        awaitEachGesture {
                            // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                            // in the Initial pass to observe events before the text field consumes them
                            // in the Main pass.
                            awaitFirstDown(pass = PointerEventPass.Initial)
                            val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                            if (upEvent != null) {
                                showDatePickerModal = true
                            }
                        }
                    }
            )
        }

        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }

    if (showDatePickerModal) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.7F))
        ) {
            DatePickerDialog(
                onDismissRequest = { showDatePickerModal = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onBirthdayChange(datePickerState.selectedDateMillis)
                            showDatePickerModal = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePickerModal = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(datePickerState)
            }
        }
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Preview(device = "id:Nexus 5X", showSystemUi = true)
@Composable
fun SettingsPageContentPreview() {
    var lifeExpectancyText by remember { mutableStateOf("75") }

    SettingsPageContent(
        modifier = Modifier.fillMaxHeight(),
        lifeExpectancyText = lifeExpectancyText,
        initialLifeExpectancy = 80,
        birthdayMillis = 0,
        onSaveClick = { },
        onLifeExpectancyChange = { lifeExpectancyText = it },
        onBirthdayChange = { }
    )
}