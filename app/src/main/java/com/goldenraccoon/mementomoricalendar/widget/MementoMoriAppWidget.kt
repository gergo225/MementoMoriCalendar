package com.goldenraccoon.mementomoricalendar.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.text.Text

class MementoMoriAppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme(colors = MementoMoriAppWidgetColorScheme.colors) {
                WidgetContent()
            }
        }
    }

    @Composable
    private fun WidgetContent() {
        Text(
            text = "Remember that you will die.",
            modifier = GlanceModifier
                .background(GlanceTheme.colors.primary)
        )
    }
}
