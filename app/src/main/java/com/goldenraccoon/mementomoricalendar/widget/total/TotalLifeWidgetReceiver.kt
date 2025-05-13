package com.goldenraccoon.mementomoricalendar.widget.total

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.goldenraccoon.mementomoricalendar.data.percentageOfLifeLived
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.widget.WidgetUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TotalLifeWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = TotalLifeWidget()

    private val coroutineScope = MainScope()

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        updateWidgetUsingLatestValues(context)
    }

    private fun updateWidgetUsingLatestValues(context: Context) {
        coroutineScope.launch {
            context.userSettingsDataStore.data.collect {
                val percentageLived = it.percentageOfLifeLived() ?: return@collect
                WidgetUtils.updateTotalLifeWidget(percentageLived, context)
            }
        }
    }
}