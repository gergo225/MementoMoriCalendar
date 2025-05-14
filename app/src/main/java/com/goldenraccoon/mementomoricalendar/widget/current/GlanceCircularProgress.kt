package com.goldenraccoon.mementomoricalendar.widget.current

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.Text
import com.goldenraccoon.mementomoricalendar.util.ColorUtil
import com.goldenraccoon.mementomoricalendar.widget.MementoMoriAppWidgetColorScheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GlanceCircularProgress(
    progress: Float,
    text: String,
    strokeWidth: Float = 200f,  // TODO: try to use .dp instead of pixels
    modifier: GlanceModifier = GlanceModifier
) {
    val context = LocalContext.current

    val boundedProgress = progress.coerceIn(0f, 1f)
    val trackColor = GlanceTheme.colors.primaryContainer.getColor(context).copy(alpha = 0.3F)
    val color = GlanceTheme.colors.primaryContainer.getColor(context)
    val endColor = GlanceTheme.colors.primary.getColor(context)

    // TODO: try to use .dp instead of pixels
    val bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val startAngle = 270f
    val sweep = boundedProgress * 360f

    canvas.drawDeterminateCircularIndicator(startAngle, 360f, trackColor, strokeWidth)
    canvas.drawDeterminateCircularIndicator(startAngle, sweep, color, endColor, strokeWidth)

    Box(
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = modifier,
            provider = ImageProvider(bitmap),
            contentScale = ContentScale.Fit,
            contentDescription = "Circular progress bar"
        )

        Text(
            text = text
        )
    }
}


private fun Canvas.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    strokeWidth: Float
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = strokeWidth / 2
    val arcDimen = width - 2 * diameterOffset

    drawArc(
        diameterOffset,
        diameterOffset,
        arcDimen + diameterOffset,
        arcDimen + diameterOffset,
        startAngle,
        sweep,
        false,
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            this.color = color.toArgb()
            this.strokeWidth = strokeWidth
        }
    )
}

private fun Canvas.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    gradientStart: Color,
    gradientEnd: Color,
    strokeWidth: Float
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = strokeWidth / 2
    val arcDimen = width - 2 * diameterOffset

    val radius = width / 2 - diameterOffset

    val startCapX =
        radius * cos(Math.toRadians(startAngle.toDouble())).toFloat() + center.x
    val startCapY =
        radius * sin(Math.toRadians(startAngle.toDouble())).toFloat() + center.x

    val endCapX =
        radius * cos(Math.toRadians(sweep.toDouble() + startAngle)).toFloat() + center.x
    val endCapY =
        radius * sin(Math.toRadians(sweep.toDouble() + startAngle)).toFloat() + center.x
    val endCapColor = ColorUtil.pointBetweenColors(gradientStart, gradientEnd, sweep / 360f)


    drawArc(
        diameterOffset,
        diameterOffset,
        arcDimen + diameterOffset,
        arcDimen + diameterOffset,
        startAngle,
        sweep,
        false,
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = gradientEnd.toArgb()
            this.strokeWidth = strokeWidth
        }
    )

    drawCircle(startCapX, startCapY, diameterOffset,
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = gradientStart.toArgb()
        }
    )

    drawCircle(endCapX, endCapY, diameterOffset,
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = endCapColor.toArgb()
        }
    )
}

private fun Canvas.drawDeterminateCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    strokeWidth: Float
) = drawCircularIndicator(startAngle, sweep, color, strokeWidth)

private fun Canvas.drawDeterminateCircularIndicator(
    startAngle: Float,
    sweep: Float,
    gradientStart: Color,
    gradientEnd: Color,
    strokeWidth: Float
) = drawCircularIndicator(startAngle, sweep, gradientStart, gradientEnd, strokeWidth)

private val Canvas.center: Offset
    get() = Offset(width / 2f, height / 2f)


@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 280, heightDp = 290)
@Composable
fun GlanceCircularProgressPreview() {
    GlanceTheme(colors = MementoMoriAppWidgetColorScheme.colors) {
        GlanceCircularProgress(
            progress = 0.7f,
            text = "70%",
            modifier = GlanceModifier.fillMaxSize()
        )
    }
}