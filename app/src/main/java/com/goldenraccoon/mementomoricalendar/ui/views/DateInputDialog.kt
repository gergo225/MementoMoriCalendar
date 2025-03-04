package com.goldenraccoon.mementomoricalendar.ui.views

import android.icu.util.Calendar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInputDialog(
    modifier: Modifier = Modifier,
    onDateSelected: (Long) -> Unit
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

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(it)
                    }
                },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text("OK")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}