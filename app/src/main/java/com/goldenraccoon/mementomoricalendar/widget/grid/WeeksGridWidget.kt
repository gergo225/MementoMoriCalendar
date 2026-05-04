package com.goldenraccoon.mementomoricalendar.widget.grid

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import com.goldenraccoon.mementomoricalendar.data.totalYears
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.data.weeksLived
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_TOTAL_YEARS_KEY
import com.goldenraccoon.mementomoricalendar.util.DataStoreConstants.WIDGET_WEEKS_LIVED_KEY
import com.goldenraccoon.mementomoricalendar.widget.MementoMoriAppWidgetColorScheme
import com.goldenraccoon.mementomoricalendar.widget.NoDataContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.File
import androidx.glance.layout.ContentScale
import androidx.compose.runtime.Composable
import androidx.glance.ColorFilter
import androidx.glance.color.DayNightColorProvider
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import com.goldenraccoon.mementomoricalendar.ui.theme.DarkColors
import com.goldenraccoon.mementomoricalendar.ui.theme.LightColors

class WeeksGridWidget : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<*>
        get() = WeeksGridGlanceStateDefinition

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

        val totalYearsString = preferences[stringPreferencesKey(WIDGET_TOTAL_YEARS_KEY)]
        val weeksLivedString = preferences[stringPreferencesKey(WIDGET_WEEKS_LIVED_KEY)]

        if (totalYearsString != null && weeksLivedString != null) {
            val totalYears = totalYearsString.toIntOrNull() ?: 80
            val weeksLived = weeksLivedString.toIntOrNull() ?: 0

            WidgetContent(
                totalYears = totalYears,
                weeksLived = weeksLived
            )
        } else {
            NoDataContent()
        }
    }

    @SuppressLint("RestrictedApi")
    @Composable
    private fun WidgetContent(
        totalYears: Int,
        weeksLived: Int
    ) {
        val bitmap = drawWeeksGridBitmap(
            totalYears = totalYears,
            filledCellCount = weeksLived,
            filledColor = Color.WHITE,
            emptyColor = Color.argb(51, 255, 255, 255),
            textColor = Color.WHITE
        )

        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(GlanceTheme.colors.background)
                .padding(vertical = 12.dp, horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                provider = ImageProvider(bitmap),
                contentDescription = "Weeks Grid",
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(
                    DayNightColorProvider(
                        day = LightColors.primary,
                        night = DarkColors.primary
                    )
                ),
                modifier = GlanceModifier.fillMaxSize()
            )
        }
    }

    private fun drawWeeksGridBitmap(
        totalYears: Int,
        filledCellCount: Int,
        filledColor: Int,
        emptyColor: Int,
        textColor: Int
    ): Bitmap {
        val yearsInUnit = 5
        val weeksInYear = 52
        val weeksInQuarter = 13
        
        val wholeUnits = totalYears / yearsInUnit
        val remainingYears = totalYears % yearsInUnit
        val totalUnits = wholeUnits + if (remainingYears > 0) 1 else 0
        val totalRows = totalYears

        // Use a fixed high-resolution coordinate system
        val cellSize = 12f
        val cellGap = 2f
        val groupSeparatorSize = 12f
        val middleSeparatorSize = 10f
        val quarterSeparatorSize = 4f
        val labelSpace = 64f // space on the left for text

        val fixedHorizontalSpace = 51 * cellGap + middleSeparatorSize + 2 * quarterSeparatorSize 
        val fixedVerticalSpace = (totalRows - totalUnits) * cellGap + (totalUnits - 1) * groupSeparatorSize

        val actualWidth = (52 * cellSize + fixedHorizontalSpace + labelSpace).toInt()
        val actualHeight = (totalRows * cellSize + fixedVerticalSpace).toInt()

        val bitmap = createBitmap(actualWidth, actualHeight)
        val canvas = Canvas(bitmap)

        val paint = Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        
        val textPaint = Paint().apply {
            color = textColor
            isAntiAlias = true
            textSize = 36f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.RIGHT
        }

        var currentY = 0f

        // Draw groups
        for (unit in 0 until totalUnits) {
            val rowsInThisUnit = if (unit == wholeUnits) remainingYears else yearsInUnit
            val unitHeight = rowsInThisUnit * cellSize + (rowsInThisUnit - 1) * cellGap
            val centerY = currentY + unitHeight / 2f

            if (unit != wholeUnits) {
                val label = ((unit + 1) * yearsInUnit).toString()
                val textY = centerY - (textPaint.descent() + textPaint.ascent()) / 2f
                val textX = labelSpace - 16f
                canvas.drawText(label, textX, textY, textPaint)
            }

            for (row in 0 until rowsInThisUnit) {
                val yearIndex = unit * yearsInUnit + row
                var currentX = labelSpace
                
                for (week in 0 until weeksInYear) {
                    val cellIndex = yearIndex * weeksInYear + week
                    
                    if (cellIndex < filledCellCount) {
                        paint.color = filledColor
                    } else {
                        paint.color = emptyColor
                    }

                    canvas.drawRect(currentX, currentY, currentX + cellSize, currentY + cellSize, paint)

                    currentX += cellSize
                    
                    if (week != weeksInYear - 1) {
                        currentX += cellGap
                        if ((week + 1) % weeksInQuarter == 0) {
                            if ((week + 1) == weeksInYear / 2) {
                                currentX += (middleSeparatorSize - cellGap)
                            } else {
                                currentX += (quarterSeparatorSize - cellGap)
                            }
                        }
                    }
                }
                
                currentY += cellSize
                if (row != rowsInThisUnit - 1) {
                    currentY += cellGap
                }
            }
            if (unit != totalUnits - 1) {
                currentY += groupSeparatorSize
            }
        }

        return bitmap
    }

    @OptIn(ExperimentalGlancePreviewApi::class)
    @Preview(widthDp = 250, heightDp = 300)
    @Composable
    private fun WidgetContentPreview() {
        GlanceTheme(colors = MementoMoriAppWidgetColorScheme.colors) {
            WidgetContent(80, 1732)
        }
    }
}

object WeeksGridGlanceStateDefinition : GlanceStateDefinition<Preferences> {
    private const val FILE_NAME = "weeks_grid_widget_preferences"

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<Preferences> {
        val dataStore = context.dataStore
        val isDataSet = dataStore.isDataSet.first()
        if (!isDataSet) {
            val totalYears = context.userSettingsDataStore.data.first().totalYears()
            val weeksLived = context.userSettingsDataStore.data.first().weeksLived()
            if (totalYears != null && weeksLived != null) {
                dataStore.edit { preferences ->
                    preferences[stringPreferencesKey(WIDGET_TOTAL_YEARS_KEY)] = totalYears.toString()
                    preferences[stringPreferencesKey(WIDGET_WEEKS_LIVED_KEY)] = weeksLived.toString()
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

private val DataStore<Preferences>.isDataSet: Flow<Boolean>
    get() {
        return data.map {
            it.contains(stringPreferencesKey(WIDGET_TOTAL_YEARS_KEY)) && 
            it.contains(stringPreferencesKey(WIDGET_WEEKS_LIVED_KEY))
        }
    }
