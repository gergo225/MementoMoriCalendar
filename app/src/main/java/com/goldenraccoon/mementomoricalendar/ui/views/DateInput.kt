package com.goldenraccoon.mementomoricalendar.ui.views

import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.goldenraccoon.mementomoricalendar.ui.theme.MementoMoriCalendarTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInput(
    modifier: Modifier = Modifier,
    onDateSelected: (Long?) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        selectableDates = PastSelectableDates
    )

    LaunchedEffect(datePickerState.selectedDateMillis) {
        onDateSelected(datePickerState.selectedDateMillis)
    }

    DatePicker(
        modifier = modifier,
        state = datePickerState,
        title = { }
    )
}

@Preview
@Composable
fun DateInputDialogPreview() {
    MementoMoriCalendarTheme {
        DateInput { }
    }
}