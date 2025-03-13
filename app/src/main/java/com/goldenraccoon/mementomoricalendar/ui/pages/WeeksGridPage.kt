package com.goldenraccoon.mementomoricalendar.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.goldenraccoon.mementomoricalendar.ui.viewmodels.WeeksGridViewModel
import com.goldenraccoon.mementomoricalendar.ui.views.TotalWeeksGrid

@Composable
fun WeeksGridPage(
    modifier: Modifier = Modifier,
    viewModel: WeeksGridViewModel = hiltViewModel()
) {
    val elapsedWeeks by viewModel.elapsedWeeks.collectAsState()

    TotalWeeksGrid(
        modifier = modifier,
        currentWeekCount = elapsedWeeks,
        totalYears = 80 // TODO: change it to be able to set custom value
    )
}