package com.goldenraccoon.mementomoricalendar.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.goldenraccoon.mementomoricalendar.ui.theme.MementoMoriCalendarTheme
import com.goldenraccoon.mementomoricalendar.util.Constants.WEEKS_IN_QUARTER
import com.goldenraccoon.mementomoricalendar.util.Constants.WEEKS_IN_YEAR

@Composable
fun WeeksGrid(
    modifier: Modifier = Modifier,
    yearsInUnit: Int = 5,
    groupSeparatorSize: Dp = 8.dp,
    totalYears: Int,
    filledCellCount: Int,
    filledCellColor: Color = MaterialTheme.colorScheme.primary,
    emptyCellColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5F)
) {
    val wholeUnits = totalYears / yearsInUnit
    Column(
        verticalArrangement = Arrangement.spacedBy(groupSeparatorSize),
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        (0..<wholeUnits).forEach { unit ->
            val filledCount = (filledCellCount - unit * yearsInUnit * WEEKS_IN_YEAR)
                .coerceIn(0, yearsInUnit * WEEKS_IN_YEAR)
            WeeksGridUnit(
                yearsInUnit = yearsInUnit,
                label = ((unit + 1) * yearsInUnit).toString(),
                filledCellCount = filledCount,
                filledCellColor = filledCellColor,
                emptyCellColor = emptyCellColor
            )
        }

        val remainingYears = totalYears.mod(yearsInUnit)
        if (remainingYears > 0) {
            val filledCount = (filledCellCount - (totalYears - remainingYears) * WEEKS_IN_YEAR)
                .coerceIn(0, yearsInUnit * WEEKS_IN_YEAR)
            WeeksGridUnit(
                yearsInUnit = remainingYears,
                filledCellCount = filledCount,
                filledCellColor = filledCellColor,
                emptyCellColor = emptyCellColor
            )
        }
    }
}

@Composable
private fun WeeksGridUnit(
    yearsInUnit: Int,
    label: String? = null,
    filledCellCount: Int,
    filledCellColor: Color,
    emptyCellColor: Color
) {
    val horizontalPadding = 32.dp

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(horizontal = horizontalPadding)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            (0..<yearsInUnit).forEach { year ->
                val filledCount = (filledCellCount - year * WEEKS_IN_YEAR)
                    .coerceIn(0, WEEKS_IN_YEAR)
                WeeksGridRow(
                    filledCellCount = filledCount,
                    filledCellColor = filledCellColor,
                    emptyCellColor = emptyCellColor
                )
            }
        }

        label?.let {
            Text(
                it,
                modifier = Modifier
                    .width(horizontalPadding)
                    .align(Alignment.CenterStart)
                    .offset(x = -horizontalPadding),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun WeeksGridRow(
    filledCellCount: Int,
    middleSeparatorSize: Dp = 10.dp,
    quarterSeparatorSize: Dp = 3.dp,
    filledCellColor: Color,
    emptyCellColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        (0..<WEEKS_IN_YEAR).forEach { week ->
            Spacer(
                modifier = Modifier
                    .weight(1F)
                    .aspectRatio(1F)
                    .background(if (week < filledCellCount) filledCellColor else emptyCellColor)
            )

            if (week % WEEKS_IN_QUARTER == WEEKS_IN_QUARTER - 1 && week != WEEKS_IN_YEAR - 1) {
                val separatorSize =
                    if (week == WEEKS_IN_YEAR / 2 - 1) middleSeparatorSize else quarterSeparatorSize
                Spacer(modifier = Modifier.width(separatorSize))
            }
        }
    }
}

@Preview(showSystemUi = true, device = "id:Nexus 5X")
@Composable
private fun WeeksGridPreview() {
    MementoMoriCalendarTheme(darkTheme = false) {
        WeeksGrid(
            yearsInUnit = 5,
            totalYears = 82,
            filledCellCount = 1698
        )
    }
}