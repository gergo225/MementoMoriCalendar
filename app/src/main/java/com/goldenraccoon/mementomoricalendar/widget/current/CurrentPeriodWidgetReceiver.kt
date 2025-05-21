package com.goldenraccoon.mementomoricalendar.widget.current

import android.appwidget.AppWidgetManager
import android.content.Context
import android.icu.util.Calendar
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.goldenraccoon.mementomoricalendar.util.percentageOfDayPassed
import com.goldenraccoon.mementomoricalendar.util.percentageOfMonthPassed
import com.goldenraccoon.mementomoricalendar.util.percentageOfWeekPassed
import com.goldenraccoon.mementomoricalendar.widget.WidgetUtils
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CurrentPeriodWidgetReceiver: GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = CurrentPeriodWidget()

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
            val calendar = Calendar.getInstance()
            val dayPercentage = (calendar.percentageOfDayPassed() * 100).roundToInt()
            val weekPercentage = (calendar.percentageOfWeekPassed() * 100).roundToInt()
            val monthPercentage = (calendar.percentageOfMonthPassed() * 100).roundToInt()

            WidgetUtils.updateCurrentPeriodWidget(
                dayPercentage = dayPercentage,
                weekPercentage = weekPercentage,
                monthPercentage = monthPercentage,
                context = context
            )
        }
    }
}