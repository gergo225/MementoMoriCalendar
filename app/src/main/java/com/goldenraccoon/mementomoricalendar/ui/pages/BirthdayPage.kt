package com.goldenraccoon.mementomoricalendar.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.goldenraccoon.mementomoricalendar.ui.viewmodels.BirthdayViewModel
import com.goldenraccoon.mementomoricalendar.ui.views.DateInputDialog
import java.util.Date

@Composable
fun BirthdayPage(
    modifier: Modifier = Modifier,
    viewModel: BirthdayViewModel = hiltViewModel(),
    onWeeksGridButtonClicked: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val birthdayMillis by viewModel.birthdayMillis.collectAsState()

    Box(
        modifier
            .background(Color.Gray)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(
                onClick = {
                    showDatePicker = true
                }
            ) {
                Text("Choose your birthday")
            }

            Text("Your birthday is: ${Date(birthdayMillis)}")

            Button(onWeeksGridButtonClicked) {
                Text("Go to Weeks Grid")
            }
        }
    }

    if (showDatePicker) {
        DateInputDialog(
            onDateSelected = { dateMillis ->
                showDatePicker = false
                viewModel.setBirthday(dateMillis)
            }
        )
    }
}