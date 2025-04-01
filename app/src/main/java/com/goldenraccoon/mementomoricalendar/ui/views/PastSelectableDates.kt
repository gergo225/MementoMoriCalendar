package com.goldenraccoon.mementomoricalendar.ui.views

import android.icu.util.Calendar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates

@OptIn(ExperimentalMaterial3Api::class)
object PastSelectableDates: SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            val currentMillis = Calendar.getInstance().timeInMillis
            return utcTimeMillis < currentMillis
        }
        override fun isSelectableYear(year: Int): Boolean {
            return year < Calendar.getInstance().get(Calendar.YEAR) + 1
        }
}