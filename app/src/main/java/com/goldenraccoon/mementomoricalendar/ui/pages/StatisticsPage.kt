package com.goldenraccoon.mementomoricalendar.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.goldenraccoon.mementomoricalendar.ui.theme.MementoMoriCalendarTheme
import com.goldenraccoon.mementomoricalendar.ui.viewmodels.StatisticsViewModel
import com.goldenraccoon.mementomoricalendar.ui.views.CustomCircularProgress
import com.goldenraccoon.mementomoricalendar.ui.views.StatItem
import com.goldenraccoon.mementomoricalendar.ui.views.StatsRowModel

@Composable
fun StatisticsPage(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val percentageLived by viewModel.percentageLived.collectAsStateWithLifecycle()
    val percentageOfDayPassed by viewModel.percentageOfDayPassed.collectAsStateWithLifecycle()
    val percentageOfWeekPassed by viewModel.percentageOfWeekPassed.collectAsStateWithLifecycle()
    val percentageOfMonthPassed by viewModel.percentageOfMonthPassed.collectAsStateWithLifecycle()

    StatisticsPageContent(
        modifier = modifier,
        percentageLived = percentageLived,
        percentageOfDay = percentageOfDayPassed,
        percentageOfWeek = percentageOfWeekPassed,
        percentageOfMonth = percentageOfMonthPassed
    )
}

@Composable
fun StatisticsPageContent(
    modifier: Modifier = Modifier,
    percentageLived: Int,
    percentageOfDay: Int,
    percentageOfWeek: Int,
    percentageOfMonth: Int
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Your life",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        CustomCircularProgress(
            modifier = Modifier.height(250.dp),
            text = "Lived: $percentageLived%",
            progress = percentageLived / 100F
        )

        Column(
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(
                text = "Current",
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SmallCircularProgress(
                    modifier = Modifier.weight(1F),
                    name = "Day",
                    percentage = percentageOfDay
                )
                SmallCircularProgress(
                    modifier = Modifier.weight(1F),
                    name = "Week",
                    percentage = percentageOfWeek
                )
                SmallCircularProgress(
                    modifier = Modifier.weight(1F),
                    name = "Month",
                    percentage = percentageOfMonth
                )
            }
        }
    }
}

@Composable
private fun SmallCircularProgress(
    modifier: Modifier = Modifier,
    name: String,
    percentage: Int
) {
    CustomCircularProgress(
        modifier = modifier
            .height(150.dp),
        text = "$name\n$percentage%",
        progress = percentage / 100F,
        strokeWidth = 18.dp
    )
}

@Preview(showSystemUi = true)
@Composable
fun StatisticsPageContentPreview() {
    StatisticsPageContent(
        modifier = Modifier.fillMaxSize(),
        percentageLived = 38,
        percentageOfWeek = 21,
        percentageOfDay = 79,
        percentageOfMonth = 53
    )
}