package com.goldenraccoon.mementomoricalendar.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.goldenraccoon.mementomoricalendar.util.Constants.WEEKS_IN_A_QUARTER
import com.goldenraccoon.mementomoricalendar.util.Constants.WEEKS_IN_A_YEAR

@Composable
fun WeeksGrid(
    modifier: Modifier = Modifier,
    yearsInUnit: Int = 5,
    groupSeparatorSize: Dp = 8.dp,
    totalYears: Int,
    filledCellCount: Int,
    filledCellColor: Color = Color.Black,
    emptyCellColor: Color = Color.Black.copy(alpha = 0.4f)
) {
    val wholeUnits = totalYears / yearsInUnit
    Column(
        verticalArrangement = Arrangement.spacedBy(groupSeparatorSize),
        modifier = modifier
    ) {
        (0..<wholeUnits).forEach { unit ->
            val filledCount = (filledCellCount - unit * yearsInUnit * WEEKS_IN_A_YEAR)
                .coerceIn(0, yearsInUnit * WEEKS_IN_A_YEAR)
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
            val filledCount = (filledCellCount - (totalYears - remainingYears) * WEEKS_IN_A_YEAR)
                .coerceIn(0, yearsInUnit * WEEKS_IN_A_YEAR)
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
    Box(
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            (0..<yearsInUnit).forEach { year ->
                val filledCount = (filledCellCount - year * WEEKS_IN_A_YEAR)
                    .coerceIn(0, WEEKS_IN_A_YEAR)
                WeeksGridRow(
                    filledCellCount = filledCount,
                    filledCellColor = filledCellColor,
                    emptyCellColor = emptyCellColor
                )
            }
        }

        label?.let {
            Text(it, modifier = Modifier.fillMaxWidth().align(Alignment.Center), textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun WeeksGridRow(
    filledCellCount: Int,
    horizontalSeparatorSize: Dp = 16.dp,
    filledCellColor: Color,
    emptyCellColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        (0..<WEEKS_IN_A_YEAR).forEach { week ->
            Spacer(
                modifier = Modifier
                    .weight(1F)
                    .aspectRatio(1F)
                    .background(if (week < filledCellCount) filledCellColor else emptyCellColor)
            )

            if (week % WEEKS_IN_A_QUARTER == WEEKS_IN_A_QUARTER - 1 && week != WEEKS_IN_A_YEAR - 1) {
                Spacer(modifier = Modifier.width(horizontalSeparatorSize))
            }
        }
    }
}

@Preview(showSystemUi = true, device = "id:Nexus 5X")
@Composable
private fun WeeksGridPreview() {
    WeeksGrid(
        yearsInUnit = 5,
        totalYears = 82,
        filledCellCount = 1698
    )
}