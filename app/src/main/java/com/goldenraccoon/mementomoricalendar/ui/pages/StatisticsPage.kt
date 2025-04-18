package com.goldenraccoon.mementomoricalendar.ui.pages

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.goldenraccoon.mementomoricalendar.ui.viewmodels.StatsRowType
import com.goldenraccoon.mementomoricalendar.ui.views.CustomCircularProgress
import com.goldenraccoon.mementomoricalendar.ui.views.SingleChoiceSegmentedButton
import com.goldenraccoon.mementomoricalendar.ui.views.StatItem
import com.goldenraccoon.mementomoricalendar.ui.views.StatsRow
import com.goldenraccoon.mementomoricalendar.ui.views.StatsRowModel
import java.util.Locale

@Composable
fun StatisticsPage(
    modifier: Modifier = Modifier,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val percentageLived by viewModel.percentageLived.collectAsStateWithLifecycle()
    val percentageOfDayPassed by viewModel.percentageOfDayPassed.collectAsStateWithLifecycle()
    val percentageOfWeekPassed by viewModel.percentageOfWeekPassed.collectAsStateWithLifecycle()
    val percentageOfMonthPassed by viewModel.percentageOfMonthPassed.collectAsStateWithLifecycle()

    val statType by viewModel.statsRowType.collectAsStateWithLifecycle()
    val statYears by viewModel.statYears.collectAsStateWithLifecycle()
    val statMonths by viewModel.statMonths.collectAsStateWithLifecycle()
    val statWeeks by viewModel.statWeeks.collectAsStateWithLifecycle()
    val statDays by viewModel.statDays.collectAsStateWithLifecycle()

    StatisticsPageContent(
        modifier = modifier,
        percentageLived = percentageLived,
        percentageOfDay = percentageOfDayPassed,
        percentageOfWeek = percentageOfWeekPassed,
        percentageOfMonth = percentageOfMonthPassed,
        stats = StatsRowModel(
            listOf(
                StatItem("Years", String.format(Locale.getDefault(), "%.1f", statYears)),
                StatItem("Months", "$statMonths"),
                StatItem("Weeks", "$statWeeks"),
                StatItem(
                    "Days",
                    DecimalFormat("#,###", DecimalFormatSymbols(Locale.getDefault())).format(
                        statDays
                    )
                )
            )
        ),
        statType = statType,
        onStatsTypeChanged = {
            viewModel.onStatsRowTypeChanged(it)
        }
    )
}

@Composable
fun StatisticsPageContent(
    modifier: Modifier = Modifier,
    percentageLived: Int,
    percentageOfDay: Int,
    percentageOfWeek: Int,
    percentageOfMonth: Int,
    stats: StatsRowModel,
    statType: StatsRowType,
    onStatsTypeChanged: (StatsRowType) -> Unit
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
            HeaderText("Current")

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

        Column(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                HeaderText("Total")

                SingleChoiceSegmentedButton(
                    modifier = Modifier.height(36.dp),
                    options = listOf("Remaining", "Lived"),
                    selectedIndex = when (statType) {
                        StatsRowType.REMAINING -> 0
                        StatsRowType.LIVED -> 1
                    },
                    onOptionSelected = {
                        when (it) {
                            0 -> onStatsTypeChanged(StatsRowType.REMAINING)
                            1 -> onStatsTypeChanged(StatsRowType.LIVED)
                            else -> {}
                        }
                    }
                )
            }

            StatsRow(
                modifier = Modifier
                    .fillMaxWidth(),
                stats = stats
            )
        }
    }

}

@Composable
private fun HeaderText(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .padding(start = 8.dp, bottom = 12.dp, top = 8.dp),
        fontSize = 24.sp,
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
private fun SmallCircularProgress(
    modifier: Modifier = Modifier,
    name: String,
    percentage: Int
) {
    CustomCircularProgress(
        modifier = modifier,
        text = "$name\n$percentage%",
        progress = percentage / 100F,
        strokeWidth = 18.dp
    )
}

@Preview(showSystemUi = true)
@Composable
fun StatisticsPageContentPreview() {
    MementoMoriCalendarTheme {
        StatisticsPageContent(
            modifier = Modifier.fillMaxSize(),
            percentageLived = 38,
            percentageOfWeek = 21,
            percentageOfDay = 79,
            percentageOfMonth = 53,
            stats = StatsRowModel(
                listOf(
                    StatItem("Years", "56.3"),
                    StatItem("Months", "676"),
                    StatItem("Weeks", "2704"),
                    StatItem("Days", "18.920")
                )
            ),
            statType = StatsRowType.REMAINING,
            onStatsTypeChanged = { }
        )
    }
}