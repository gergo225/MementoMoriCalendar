package com.goldenraccoon.mementomoricalendar.ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

@Composable
fun TotalWeeksGrid(
    modifier: Modifier = Modifier,
    currentWeekCount: Int,
    totalYears: Int,
    filledCellColor: Color = Color.Black,
    emptyCellColor: Color = Color.Black.copy(alpha = 0.4f)
) {
    val weeksInAYear = 52

    val cellHorizontalSpacing = 2.dp
    val cellVerticalSpacing = 3.dp
    val middleSplit = 16.dp
    val quarterSplit = 4.dp
    val fiveYearSplit = 6.dp

    val textMeasurer = rememberTextMeasurer()

    val style = TextStyle(
        fontSize = 14.sp,
        color = Color.Black.copy(alpha = 0.4f)
    )



    Canvas(modifier) {
        val cellHorizontalSpacingPx = cellHorizontalSpacing.roundToPx()
        val cellVerticalSpacingPx = cellVerticalSpacing.roundToPx()
        val middleSplitPx = middleSplit.roundToPx()
        val quarterSplitPx = quarterSplit.roundToPx()
        val fiveYearSplitPx = fiveYearSplit.roundToPx()
        val cellSize = (size.width - middleSplitPx - 2 * quarterSplitPx) / weeksInAYear - cellHorizontalSpacingPx
        val xPositions = Array(weeksInAYear) { 0 }

        var occupied = 0
        var lastSpace = 0
        (0..<weeksInAYear).forEach { week ->
            val extraMiddleSplit = if (week == weeksInAYear / 2) middleSplitPx else 0
            val extraQuarterSplit = if (week > 0 && week % (weeksInAYear / 4) == 0 && week != weeksInAYear / 2) quarterSplitPx else 0
            val x = min(occupied, size.width.toInt() - cellSize.toInt()) + extraMiddleSplit + extraQuarterSplit
            lastSpace = min(cellHorizontalSpacingPx, size.width.toInt() - x - cellSize.toInt())
            occupied = x + cellSize.toInt() + lastSpace
            xPositions[week] = x
        }
        occupied -= lastSpace

        if (occupied < size.width) {
            val groupPosition = size.width - occupied
            for (index in xPositions.indices) {
                xPositions[index] += (groupPosition / 2).toInt()
            }
        }


        (0..<totalYears).forEach { year ->
            val y = (cellSize + cellVerticalSpacingPx) * year + (year / 5) * fiveYearSplitPx
            (0..<weeksInAYear).forEach { week ->
                val x = xPositions[week]
                val weekInTotalYears = week + year * weeksInAYear
                val isFilled = weekInTotalYears < currentWeekCount
                drawCell(
                    topLeft = Offset(x.toFloat(), y),
                    size = cellSize,
                    color = if (isFilled) filledCellColor else emptyCellColor
                )
            }

            val isLastYear = year == totalYears - 1
            val shouldDrawLastFiveYearNumber = isLastYear && totalYears % 5 == 0
            if (year % 5 == 0 || shouldDrawLastFiveYearNumber) {
                drawFiveYearText(
                    year = if (shouldDrawLastFiveYearNumber) year + 1 else year,
                    textStyle = style,
                    cellSize = cellSize,
                    textMeasurer = textMeasurer,
                    cellVerticalSpacingPx = cellVerticalSpacingPx,
                    fiveYearSplitPx = fiveYearSplitPx
                )
            }
        }
    }

}

private fun DrawScope.drawCell(topLeft: Offset, size: Float, color: Color) {
    drawRect(
        color = color,
        topLeft = topLeft,
        size = Size(size, size)
    )
}

private fun DrawScope.drawFiveYearText(
    year: Int,
    textStyle: TextStyle,
    textMeasurer: TextMeasurer,
    cellSize: Float,
    cellVerticalSpacingPx: Int,
    fiveYearSplitPx: Int
) {
    val text = year.toString()
    val textLayoutResult = textMeasurer.measure(text, textStyle)

    val textX = center.x - textLayoutResult.size.width / 2
    val fiveCellHeight = cellSize * 5 + cellVerticalSpacingPx * 4
    val textY = fiveCellHeight / 2 + (fiveCellHeight + cellVerticalSpacingPx) * (year / 5 - 1) + (year / 5 - 1) * fiveYearSplitPx - textLayoutResult.size.height / 2

    if (textY <= size.height) {
        drawText(
            textMeasurer = textMeasurer,
            text = text,
            style = textStyle,
            topLeft = Offset(
                x = textX,
                y = textY
            )
        )
    }
}

@Preview(showSystemUi = true, device = "id:Nexus 5X")
@Composable
private fun TotalWeeksGridPreview() {
    TotalWeeksGrid(
        modifier = Modifier.fillMaxSize(),
        currentWeekCount = (20.3 * 52).toInt(),
        totalYears = 60
    )
}