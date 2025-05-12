package com.goldenraccoon.mementomoricalendar.widget.weeks

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.goldenraccoon.mementomoricalendar.data.remainingWeeks
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.widget.WidgetUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class RemainingWeeksWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = RemainingWeeksWidget()

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
                val remainingWeeks = it.remainingWeeks() ?: return@collect
                WidgetUtils.updateRemainingWeeksWidgets(remainingWeeks, context)
            }
        }
    }
}
