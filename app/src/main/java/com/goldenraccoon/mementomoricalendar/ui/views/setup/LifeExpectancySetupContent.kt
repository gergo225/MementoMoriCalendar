package com.goldenraccoon.mementomoricalendar.ui.views.setup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.goldenraccoon.mementomoricalendar.ui.theme.MementoMoriCalendarTheme
import com.goldenraccoon.mementomoricalendar.util.Constants

@Composable
fun LifeExpectancySetupContent(
    modifier: Modifier = Modifier,
    lifeExpectancyText: String,
    onLifeExpectancyChange: (String) -> Unit,
    lifeExpectancyWarning: String? = null,
    isButtonEnabled: Boolean,
    onFinishClicked: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Set expected years to live",
            modifier = Modifier.padding(top = 24.dp),
            fontSize = 24.sp
        )

        OutlinedTextField(
            value = lifeExpectancyText,
            onValueChange = onLifeExpectancyChange,
            label = { Text("Life Expectancy") },
            placeholder = { Text(Constants.DEFAULT_LIFE_EXPECTANCY_YEARS.toString(), color = Color.Gray) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            isError = lifeExpectancyWarning != null,
            supportingText = {
                if (lifeExpectancyWarning != null) {
                    Text(
                        text = lifeExpectancyWarning,
                        color = MaterialTheme.colorScheme.errorContainer
                    )
                }
            },
            trailingIcon = {
                if (lifeExpectancyWarning != null) {
                    Icon(Icons.Filled.Warning, "error", tint = MaterialTheme.colorScheme.errorContainer)
                }
            }
        )

        Spacer(modifier = Modifier.weight(1F))

        SetupPageSubmitButton(
            onClick = onFinishClicked,
            isEnabled = isButtonEnabled,
            text = "Finish",
            icon = Icons.Filled.Check,
            iconDescription = "Check icon",
            modifier = Modifier.imePadding()
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun LifeExpectancySetupContentPreview() {
    MementoMoriCalendarTheme {
        LifeExpectancySetupContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            lifeExpectancyText = "75",
            onLifeExpectancyChange = { },
            isButtonEnabled = true,
            onFinishClicked = { }
        )
    }
}