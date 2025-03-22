package com.goldenraccoon.mementomoricalendar.ui.views

import android.icu.util.Calendar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInput(
    modifier: Modifier = Modifier,
    onDateSelected: (Long?) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val currentMillis = Calendar.getInstance().timeInMillis
                return utcTimeMillis < currentMillis
            }
            override fun isSelectableYear(year: Int): Boolean {
                return year < Calendar.getInstance().get(Calendar.YEAR) + 1
            }
        }
    )

    LaunchedEffect(datePickerState.selectedDateMillis) {
        onDateSelected(datePickerState.selectedDateMillis)
    }

    DatePicker(
        modifier = modifier,
        state = datePickerState,
        colors = DatePickerDefaults.colors(containerColor = Color.Gray),
        title = { }
    )
}

@Preview
@Composable
fun DateInputDialogPreview() {
    DateInput { }
}