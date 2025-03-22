package com.goldenraccoon.mementomoricalendar.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.goldenraccoon.mementomoricalendar.ui.viewmodels.BirthdayViewModel
import com.goldenraccoon.mementomoricalendar.ui.views.DateInput

@Composable
fun BirthdayPage(
    modifier: Modifier = Modifier,
    viewModel: BirthdayViewModel = hiltViewModel(),
    onContinueClicked: () -> Unit
) {
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }

    Box(
        modifier
            .background(Color.Gray)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Choose your birthday",
                modifier = Modifier.padding(top = 24.dp),
                fontSize = 24.sp
            )

            DateInput(
                modifier = Modifier.weight(1F),
                onDateSelected = { dateMillis ->
                    selectedDateMillis = dateMillis
                }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                onClick = {
                    val birthdayMillis = selectedDateMillis ?: return@Button
                    viewModel.setBirthday(birthdayMillis)
                    onContinueClicked()
                },
                enabled = selectedDateMillis != null
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Continue")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = "Arrow pointing to the right"
                    )
                }
            }
        }
    }
}