package com.goldenraccoon.mementomoricalendar.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
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
        Box(
            modifier = GlanceModifier
                .padding(4.dp)
                .background(GlanceTheme.colors.widgetBackground)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Remember that you will die.")
        }
    }
}
