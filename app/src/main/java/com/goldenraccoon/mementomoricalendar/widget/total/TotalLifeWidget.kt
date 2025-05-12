package com.goldenraccoon.mementomoricalendar.widget.total

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
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.FixedColorProvider
import com.goldenraccoon.mementomoricalendar.data.percentageOfLifeLived
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_PERCENTAGE_LIVED_KEY
import com.goldenraccoon.mementomoricalendar.widget.MementoMoriAppWidgetColorScheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File
import kotlin.math.roundToInt

class TotalLifeWidget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<*>
        get() = TotalLifeGlanceStateDefinition

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
        val percentageLivedString = preferences[stringPreferencesKey(WIDGET_PERCENTAGE_LIVED_KEY)] ?: "0"
        val percentageLived = (percentageLivedString.toFloatOrNull() ?: 0f) / 100

        WidgetContent(
            lifeProgress = percentageLived
        )
    }

    @Composable
    private fun WidgetContent(
        lifeProgress: Float
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
                text = "Total Life",
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

            LinearProgressIndicator(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .height(32.dp),
                progress = lifeProgress,
                color = GlanceTheme.colors.primary,
                backgroundColor = FixedColorProvider(
                    GlanceTheme.colors.primary.getColor(context).copy(alpha = 0.2f)
                )
            )

            Box(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(end = 4.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "${(lifeProgress * 100).roundToInt()}%",
                    maxLines = 1,
                    style = TextStyle(color = GlanceTheme.colors.primary)
                )
            }
        }
    }

    @OptIn(ExperimentalGlancePreviewApi::class)
    @Preview(widthDp = 180, heightDp = 90)
    @Composable
    private fun WidgetContentPreview() {
        GlanceTheme(colors = MementoMoriAppWidgetColorScheme.colors) {
            WidgetContent(lifeProgress = 0.3f)
        }
    }
}

object TotalLifeGlanceStateDefinition : GlanceStateDefinition<Preferences> {
    private const val FILE_NAME = "total_life_widget_preferences"

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<Preferences> {
        val dataStore = context.dataStore
        val isPercentageLivedSet = dataStore.isPercentageLivedSet.first()
        if (!isPercentageLivedSet) {
            val percentageLived = context.userSettingsDataStore.data.first().percentageOfLifeLived()
            if (percentageLived != null) {
                dataStore.edit { preferences ->
                    preferences[stringPreferencesKey(WIDGET_PERCENTAGE_LIVED_KEY)] =
                        percentageLived.toString()
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

private val DataStore<Preferences>.isPercentageLivedSet: Flow<Boolean>
    get() {
        return data.map {
            it.contains(stringPreferencesKey(WIDGET_PERCENTAGE_LIVED_KEY))
        }
    }