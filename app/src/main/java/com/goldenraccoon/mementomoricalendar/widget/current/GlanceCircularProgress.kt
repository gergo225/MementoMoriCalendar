package com.goldenraccoon.mementomoricalendar.widget.current

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.SweepGradient
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import com.goldenraccoon.mementomoricalendar.util.ColorUtil
import com.goldenraccoon.mementomoricalendar.widget.MementoMoriAppWidgetColorScheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GlanceCircularProgress(
    progress: Float,
    text: String,
    strokeWidthRatio: Float = 0.15f,
    modifier: GlanceModifier = GlanceModifier
) {
    val boundedProgress = progress.coerceIn(0f, 1f)
    val color = Color(red = 0.71f, green = 0.43f, blue = 0.3f)
    val trackColor = color.copy(alpha = 0.2f)

    val bitmapSize = 1000
    val bitmap = Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val startAngle = 270f
    val sweep = boundedProgress * 360f

    val strokeWidth = bitmapSize * strokeWidthRatio
    canvas.drawDeterminateCircularIndicator(startAngle, 360f, trackColor, strokeWidth)
    canvas.drawDeterminateCircularIndicator(startAngle, sweep, color, color, strokeWidth)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Image(
            provider = ImageProvider(bitmap),
            contentScale = ContentScale.Fit,
            contentDescription = "Circular progress bar"
        )

        Text(
            text = text,
            style = TextStyle(
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                color = GlanceTheme.colors.primary
            )
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


    val gradient = SweepGradient(
        width / 2f, height / 2f,
        gradientStart.toArgb(), gradientEnd.toArgb()
    )
    val rotationMatrix = Matrix().apply {
        postRotate(-90f, width / 2f, height / 2f)
    }
    gradient.setLocalMatrix(rotationMatrix)

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
            this.strokeWidth = strokeWidth
            shader = gradient
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
            text = "70%\nMonth",
            modifier = GlanceModifier.fillMaxSize()
        )
    }
}