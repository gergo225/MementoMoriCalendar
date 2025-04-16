package com.goldenraccoon.mementomoricalendar.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.goldenraccoon.mementomoricalendar.R
import com.goldenraccoon.mementomoricalendar.ui.viewmodels.WeeksGridViewModel
import com.goldenraccoon.mementomoricalendar.ui.views.WeeksGrid
import com.goldenraccoon.mementomoricalendar.util.Constants
import com.goldenraccoon.mementomoricalendar.util.Constants.DEFAULT_LIFE_EXPECTANCY_YEARS

@Composable
fun WeeksGridPage(
    modifier: Modifier = Modifier,
    viewModel: WeeksGridViewModel = hiltViewModel(),
    onNavigateToSetupPage: () -> Unit,
    onNavigateToSettingsPage: () -> Unit = {},
    onNavigateToStatisticsPage: () -> Unit = {}
) {
    val elapsedWeeks by viewModel.elapsedWeeks.collectAsState()
    val lifeExpectancyYears by viewModel.lifeExpectancyYears.collectAsState()
    val shouldShowSetupPage by viewModel.shouldShowSetupPage.collectAsState()

    LaunchedEffect(shouldShowSetupPage) {
        if (shouldShowSetupPage == true) {
            onNavigateToSetupPage()
        }
    }

    if (shouldShowSetupPage == false) {
        WeeksGridContent(
            modifier = modifier,
            elapsedWeeks = elapsedWeeks,
            totalYears = lifeExpectancyYears,
            onSettingsClicked = onNavigateToSettingsPage,
            onStatisticsClicked = onNavigateToStatisticsPage
        )
    }
}

@Composable
fun WeeksGridContent(
    modifier: Modifier = Modifier,
    elapsedWeeks: Int,
    totalYears: Int = DEFAULT_LIFE_EXPECTANCY_YEARS,
    onSettingsClicked: () -> Unit = {},
    onStatisticsClicked: () -> Unit = {}
) {
    val totalWeeks = totalYears * Constants.WEEKS_IN_YEAR
    val remainingWeeks = totalWeeks - elapsedWeeks

    Column(verticalArrangement = Arrangement.SpaceBetween) {
        TitleBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 4.dp),
            onSettingsClicked = onSettingsClicked,
            onStatisticsClicked = onStatisticsClicked
        )

        WeeksGrid(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .weight(1F),
            filledCellCount = elapsedWeeks,
            totalYears = totalYears
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = "Weeks Left: $remainingWeeks",
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TitleBar(
    modifier: Modifier = Modifier,
    onSettingsClicked: () -> Unit = {},
    onStatisticsClicked: () -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onSettingsClicked
        ) {
            Icon(painter = painterResource(R.drawable.discover_tune), contentDescription = "Settings")
        }

        Text(
            text = "Memento Mori".uppercase(),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )

        IconButton(
            onClick = onStatisticsClicked
        ) {
            Icon(imageVector = Icons.Default.BarChart, contentDescription = "Statistics")
        }
    }
}

@Preview(showSystemUi = true, device = "id:Nexus 5X")
@Composable
fun WeeksGridContentPreview() {
    WeeksGridContent(
        elapsedWeeks = 1268,
        modifier = Modifier
    )
}
