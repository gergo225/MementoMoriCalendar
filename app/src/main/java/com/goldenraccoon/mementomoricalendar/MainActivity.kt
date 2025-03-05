package com.goldenraccoon.mementomoricalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.goldenraccoon.mementomoricalendar.data.UserSettingsRepository
import com.goldenraccoon.mementomoricalendar.data.userSettingsDataStore
import com.goldenraccoon.mementomoricalendar.ui.pages.BirthdayPage
import com.goldenraccoon.mementomoricalendar.ui.theme.MementoMoriCalendarTheme
import com.goldenraccoon.mementomoricalendar.ui.viewmodels.BirthdayViewModel
import com.goldenraccoon.mementomoricalendar.ui.viewmodels.BirthdayViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MementoMoriCalendarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BirthdayPage(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = ViewModelProvider(
                            this,
                            BirthdayViewModelFactory(UserSettingsRepository(userSettingsDataStore))
                        )[BirthdayViewModel::class.java]
                    )
                }
            }
        }
    }
}
