package com.goldenraccoon.mementomoricalendar.widget.weeks

import android.content.Context
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
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.goldenraccoon.mementomoricalendar.data.remainingWeeks
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_REMAINING_WEEKS_KEY
import com.goldenraccoon.mementomoricalendar.widget.MementoMoriAppWidgetColorScheme
import com.goldenraccoon.mementomoricalendar.widget.NoDataContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File

class RemainingWeeksWidget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<*>
        get() = RemainingWeeksGlanceStateDefinition

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
        val remainingWeeks =
            preferences[stringPreferencesKey(WIDGET_REMAINING_WEEKS_KEY)]

        if (remainingWeeks != null) {
            WidgetContent(remainingWeeks)
        } else {
            NoDataContent()
        }
    }

    @Composable
    private fun WidgetContent(
        remainingWeeks: String
    ) {
        Column(
            modifier = GlanceModifier
                .padding(4.dp)
                .background(GlanceTheme.colors.background)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = remainingWeeks,
                style = TextStyle(fontSize = 24.sp, color = GlanceTheme.colors.primary)
            )
            Text(
                text = "Weeks Remaining",
                style = TextStyle(color = GlanceTheme.colors.primary)
            )
        }
    }

    @OptIn(ExperimentalGlancePreviewApi::class)
    @Preview(widthDp = 180, heightDp = 90)
    @Composable
    private fun WidgetPreview() {
        GlanceTheme(colors = MementoMoriAppWidgetColorScheme.colors) {
            WidgetContent("5392")
        }
    }
}

object RemainingWeeksGlanceStateDefinition : GlanceStateDefinition<Preferences> {
    private const val FILE_NAME = "remaining_weeks_widget_preference"

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<Preferences> {
        val dataStore = context.dataStore
        val isRemainingWeeksSet = dataStore.isRemainingWeeksSet.first()
        if (!isRemainingWeeksSet) {
            val remainingWeeks = context.userSettingsDataStore.data.first().remainingWeeks()
            if (remainingWeeks != null) {
                dataStore.edit { preferences ->
                    preferences[stringPreferencesKey(WIDGET_REMAINING_WEEKS_KEY)] =
                        remainingWeeks.toString()
                }
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

private val DataStore<Preferences>.isRemainingWeeksSet: Flow<Boolean>
    get() {
        return data.map {
            it.contains(stringPreferencesKey(WIDGET_REMAINING_WEEKS_KEY))
        }
    }
