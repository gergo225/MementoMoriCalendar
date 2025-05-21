package com.goldenraccoon.mementomoricalendar.widget.current

import android.content.Context
import android.icu.util.Calendar
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.FixedColorProvider
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_PERCENTAGE_OF_DAY_KEY
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_PERCENTAGE_OF_MONTH_KEY
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_PERCENTAGE_OF_WEEK_KEY
import com.goldenraccoon.mementomoricalendar.util.percentageOfDayPassed
import com.goldenraccoon.mementomoricalendar.util.percentageOfMonthPassed
import com.goldenraccoon.mementomoricalendar.util.percentageOfWeekPassed
import com.goldenraccoon.mementomoricalendar.widget.MementoMoriAppWidgetColorScheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File
import kotlin.math.roundToInt

class CurrentPeriodWidget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<*>
        get() = CurrentPeriodGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme(colors = MementoMoriAppWidgetColorScheme.colors) {
                Widget()
            }
        }
    }

    @Composable
    private fun Widget() {
        val preferences = currentState<Preferences>()
        val dayPercentageString = preferences[stringPreferencesKey(WIDGET_PERCENTAGE_OF_DAY_KEY)] ?: "0"
        val dayPercentage = (dayPercentageString.toFloatOrNull() ?: 0f) / 100
        val weekPercentageString = preferences[stringPreferencesKey(WIDGET_PERCENTAGE_OF_WEEK_KEY)] ?: "0"
        val weekPercentage = (weekPercentageString.toFloatOrNull() ?: 0f) / 100
        val monthPercentageString = preferences[stringPreferencesKey(WIDGET_PERCENTAGE_OF_MONTH_KEY)] ?: "0"
        val monthPercentage = (monthPercentageString.toFloatOrNull() ?: 0f) / 100

        WidgetContent(
            dayPercentage = dayPercentage,
            weekPercentage = weekPercentage,
            monthPercentage = monthPercentage
        )
    }

    @Composable
    private fun WidgetContent(
        dayPercentage: Float,
        weekPercentage: Float,
        monthPercentage: Float
    ) {
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
                    progress = dayPercentage,
                    text = "Day\n${(dayPercentage * 100).roundToInt()}%",
                    strokeWidthRatio = 0.1f
                )

                Spacer(modifier = spacerModifier)

                GlanceCircularProgress(
                    modifier = modifier,
                    progress = weekPercentage,
                    text = "Week\n${(weekPercentage * 100).roundToInt()}%",
                    strokeWidthRatio = 0.1f
                )

                Spacer(modifier = spacerModifier)

                GlanceCircularProgress(
                    modifier = modifier,
                    progress = monthPercentage,
                    text = "Month\n${(monthPercentage * 100).roundToInt()}%",
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
            WidgetContent(
                dayPercentage = 0.3f,
                weekPercentage = 0.8f,
                monthPercentage = 0.46f
            )
        }
    }
}

object CurrentPeriodGlanceStateDefinition : GlanceStateDefinition<Preferences> {
    private const val FILE_NAME = "current_period_widget_preferences"

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<Preferences> {
        val dataStore = context.dataStore
        val isCurrentPeriodSet = dataStore.isCurrentPeriodSet.first()
        if (!isCurrentPeriodSet) {
            val calendar = Calendar.getInstance()
            val percentageOfDay = (calendar.percentageOfDayPassed() * 100).roundToInt()
            val percentageOfWeek = (calendar.percentageOfWeekPassed() * 100).roundToInt()
            val percentageOfMonth = (calendar.percentageOfMonthPassed() * 100).roundToInt()

            dataStore.edit { preferences ->
                preferences[stringPreferencesKey(WIDGET_PERCENTAGE_OF_DAY_KEY)] = percentageOfDay.toString()
                preferences[stringPreferencesKey(WIDGET_PERCENTAGE_OF_WEEK_KEY)] = percentageOfWeek.toString()
                preferences[stringPreferencesKey(WIDGET_PERCENTAGE_OF_MONTH_KEY)] = percentageOfMonth.toString()
            }
        }

        return dataStore
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return File(context.applicationContext.filesDir, "datastore/$FILE_NAME")
    }

    private val Context.dataStore: DataStore<Preferences>
            by preferencesDataStore(name = FILE_NAME)
}

private val DataStore<Preferences>.isCurrentPeriodSet: Flow<Boolean>
    get() {
        return data.map {
            it.contains(stringPreferencesKey(WIDGET_PERCENTAGE_OF_DAY_KEY)) &&
                    it.contains(stringPreferencesKey(WIDGET_PERCENTAGE_OF_WEEK_KEY)) &&
                    it.contains(stringPreferencesKey(WIDGET_PERCENTAGE_OF_MONTH_KEY))
        }
    }