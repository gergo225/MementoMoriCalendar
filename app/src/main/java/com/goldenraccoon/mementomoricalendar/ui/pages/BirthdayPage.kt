package com.goldenraccoon.mementomoricalendar.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import com.goldenraccoon.mementomoricalendar.ui.views.DateInputDialog

@Composable
fun BirthdayPage(modifier: Modifier = Modifier) {
    var showDatePicker by remember { mutableStateOf(false) }

    Box(
        modifier
            .fillMaxSize()
            .background(Color.Gray)
    ) {
        TextButton(
            onClick = {
                showDatePicker = true
            },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("Choose your birthday")
        }
    }

    if (showDatePicker) {
        DateInputDialog {
            showDatePicker = false
        }
    }
}