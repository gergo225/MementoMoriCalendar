package com.goldenraccoon.mementomoricalendar.ui.pages

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.goldenraccoon.mementomoricalendar.ui.viewmodels.WeeksGridViewModel
import com.goldenraccoon.mementomoricalendar.ui.views.WeeksGrid

@Composable
fun WeeksGridPage(
    modifier: Modifier = Modifier,
    viewModel: WeeksGridViewModel = hiltViewModel()
) {
    val elapsedWeeks by viewModel.elapsedWeeks.collectAsState()

    WeeksGrid(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        filledCellCount = elapsedWeeks,
        totalYears = 180 // TODO: change it to be able to set custom value
    )
}