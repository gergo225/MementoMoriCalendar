package com.goldenraccoon.mementomoricalendar.widget.current

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.FixedColorProvider
import com.goldenraccoon.mementomoricalendar.widget.MementoMoriAppWidgetColorScheme

class CurrentPeriodWidget: GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme(colors = MementoMoriAppWidgetColorScheme.colors) {
                Widget()
            }
        }
    }

    @Composable
    private fun Widget() {
        WidgetContent()
    }

    @Composable
    private fun WidgetContent() {
        val context = LocalContext.current

        Column(
            modifier = GlanceModifier
                .padding(vertical = 4.dp)
                .padding(horizontal = 8.dp)
                .background(GlanceTheme.colors.background)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Current",
                maxLines = 1,
                style = TextStyle(
                    color = FixedColorProvider(
                        // TODO: fix: when changing light/dark mode, colors using ".getColor" are not updated
                        GlanceTheme.colors.primary.getColor(context).copy(alpha = 0.6f)
                    ),
                    fontSize = 12.sp
                ),
                modifier = GlanceModifier.padding(bottom = 4.dp)
            )

            Row(
                modifier = GlanceModifier.fillMaxSize()
            ) {
                val modifier = GlanceModifier.defaultWeight()
                val spacerModifier = GlanceModifier.width(4.dp)

                GlanceCircularProgress(
                    modifier = modifier,
                    progress = 0.3f,
                    text = "Day\n30%",
                    strokeWidthRatio = 0.1f
                )

                Spacer(modifier = spacerModifier)

                GlanceCircularProgress(
                    modifier = modifier,
                    progress = 0.8f,
                    text = "Week\n80%",
                    strokeWidthRatio = 0.1f
                )

                Spacer(modifier = spacerModifier)

                GlanceCircularProgress(
                    modifier = modifier,
                    progress = 0.46f,
                    text = "Month\n46%",
                    strokeWidthRatio = 0.1f
                )
            }
        }
    }

    @OptIn(ExperimentalGlancePreviewApi::class)
    @Preview(widthDp = 180, heightDp = 90)
    @Composable
    private fun WidgetContentPreview() {
        GlanceTheme(colors = MementoMoriAppWidgetColorScheme.colors) {
            WidgetContent()
        }
    }
}