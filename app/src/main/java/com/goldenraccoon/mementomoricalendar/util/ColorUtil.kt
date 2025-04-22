package com.goldenraccoon.mementomoricalendar.util

import androidx.compose.ui.graphics.Color

object ColorUtil {
    private fun pointBetweenColors(from: Float, to: Float, percent: Float): Float =
        from + percent.coerceIn(0f, 1f) * (to - from)

    fun pointBetweenColors(from: Color, to: Color, percent: Float) =
        Color(
            red = pointBetweenColors(from.red, to.red, percent),
            green = pointBetweenColors(from.green, to.green, percent),
            blue = pointBetweenColors(from.blue, to.blue, percent),
            alpha = pointBetweenColors(from.alpha, to.alpha, percent),
        )
}
