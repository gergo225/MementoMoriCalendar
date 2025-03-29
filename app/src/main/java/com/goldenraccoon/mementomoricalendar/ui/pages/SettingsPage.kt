package com.goldenraccoon.mementomoricalendar.ui.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.goldenraccoon.mementomoricalendar.ui.viewmodels.SettingsViewModel

@Composable
fun SettingsPage(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val initialLifeExpectancy by viewModel.lifeExpectancyYears.collectAsStateWithLifecycle()

    SettingsPageContent(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp),
        lifeExpectancyText = viewModel.lifeExpectancyInput,
        initialLifeExpectancy = initialLifeExpectancy,
        onLifeExpectancyChange = {
            viewModel.onLifeExpectancyInputChange(it)
        },
        onSaveClick = {
            viewModel.saveSettings()
            onNavigateBack()
        }
    )
}

@Composable
fun SettingsPageContent(
    modifier: Modifier = Modifier,
    lifeExpectancyText: String,
    initialLifeExpectancy: Int,
    onLifeExpectancyChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = lifeExpectancyText,
            onValueChange = onLifeExpectancyChange,
            label = { Text("Life Expectancy") },
            placeholder = { Text(initialLifeExpectancy.toString()) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

@Preview(device = "id:Nexus 5X", showSystemUi = true)
@Composable
fun SettingsPageContentPreview() {
    var lifeExpectancyText by remember { mutableStateOf("75") }
    SettingsPageContent(
        lifeExpectancyText = lifeExpectancyText,
        initialLifeExpectancy = 80,
        onSaveClick = { },
        onLifeExpectancyChange = { lifeExpectancyText = it }
    )
}