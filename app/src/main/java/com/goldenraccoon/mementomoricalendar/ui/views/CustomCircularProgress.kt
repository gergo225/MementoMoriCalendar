package com.goldenraccoon.mementomoricalendar.ui.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.goldenraccoon.mementomoricalendar.ui.theme.MementoMoriCalendarTheme
import com.goldenraccoon.mementomoricalendar.util.ColorUtil
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CustomCircularProgress(
    modifier: Modifier = Modifier,
    progress: Float,
    text: String,
    strokeWidth: Dp = 24.dp
) {
    val boundedProgress = progress.coerceIn(0f, 1f)
    val trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3F)
    val color = MaterialTheme.colorScheme.primaryContainer
    val endColor = MaterialTheme.colorScheme.primary
    val backgroundStroke = with(LocalDensity.current) {
        Stroke(
            width = strokeWidth.toPx(),
            cap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap
        )
    }
    val progressStroke = with(LocalDensity.current) {
        Stroke(
            width = strokeWidth.toPx(),
            cap = ProgressIndicatorDefaults.LinearStrokeCap
        )
    }

    Box(
        modifier = modifier.aspectRatio(1F),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .progressSemantics(boundedProgress),

            ) {
            val startAngle = 270f
            val sweep = boundedProgress * 360f
            drawDeterminateCircularIndicator(startAngle, 360f, trackColor, backgroundStroke)
            drawDeterminateCircularIndicator(startAngle, sweep, color, endColor, progressStroke)
        }

        Text(
            text = text,
            textAlign = TextAlign.Center
        )
    }
}

private fun DrawScope.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}

private fun DrawScope.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    gradientStart: Color,
    gradientEnd: Color,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset

    val radius = size.width / 2 - diameterOffset

    val startCapX =
        radius * cos(Math.toRadians(startAngle.toDouble() + 90)).toFloat() + size.center.x
    val startCapY =
        radius * sin(Math.toRadians(startAngle.toDouble() + 90)).toFloat() + size.center.x

    val endCapX =
        radius * cos(Math.toRadians(sweep.toDouble() + startAngle + 90)).toFloat() + size.center.x
    val endCapY =
        radius * sin(Math.toRadians(sweep.toDouble() + startAngle + 90)).toFloat() + size.center.x
    val endCapColor = ColorUtil.pointBetweenColors(gradientStart, gradientEnd, sweep / 360f)

    rotate(-90f) {
        drawArc(
            brush = Brush.sweepGradient(listOf(gradientStart, gradientEnd)),
            startAngle = startAngle + 90,
            sweepAngle = sweep,
            useCenter = false,
            topLeft = Offset(diameterOffset, diameterOffset),
            size = Size(arcDimen, arcDimen),
            style = stroke
        )

        drawCircle(gradientStart, radius = diameterOffset, center = Offset(startCapX, startCapY))

        drawCircle(endCapColor, radius = diameterOffset, center = Offset(endCapX, endCapY))
    }
}

private fun DrawScope.drawDeterminateCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) = drawCircularIndicator(startAngle, sweep, color, stroke)

private fun DrawScope.drawDeterminateCircularIndicator(
    startAngle: Float,
    sweep: Float,
    gradientStart: Color,
    gradientEnd: Color,
    stroke: Stroke
) = drawCircularIndicator(startAngle, sweep, gradientStart, gradientEnd, stroke)

@Preview(showSystemUi = true)
@Composable
fun CustomCircularProgressPreview() {
    MementoMoriCalendarTheme {
        CustomCircularProgress(
            modifier = Modifier.size(300.dp),
            progress = 0.44F,
            text = "Lived: 44%"
        )
    }
}