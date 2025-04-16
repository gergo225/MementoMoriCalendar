package com.goldenraccoon.mementomoricalendar.ui.views.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.goldenraccoon.mementomoricalendar.ui.theme.MementoMoriCalendarTheme
import com.goldenraccoon.mementomoricalendar.ui.views.DateInput


@Composable
fun BirthdaySetupContent(
    modifier: Modifier = Modifier,
    onDateSelected: (Long?) -> Unit,
    isButtonEnabled: Boolean,
    onContinueClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Text(
            "Choose your birthday",
            modifier = Modifier.padding(top = 24.dp),
            fontSize = 24.sp
        )

        DateInput(
            modifier = Modifier.weight(1F),
            onDateSelected = onDateSelected
        )

        SetupPageSubmitButton(
            onClick = onContinueClicked,
            isEnabled = isButtonEnabled,
            text = "Continue",
            icon = Icons.AutoMirrored.Default.ArrowForward,
            iconDescription = "Arrow pointing to the right"
        )
    }
}

@Preview
@Composable
fun BirthdayPageContentPreview() {
    MementoMoriCalendarTheme {
        BirthdaySetupContent(
            modifier = Modifier.fillMaxSize().background(Color.White),
            onDateSelected = { },
            isButtonEnabled = true,
            onContinueClicked = {}
        )
    }
}
