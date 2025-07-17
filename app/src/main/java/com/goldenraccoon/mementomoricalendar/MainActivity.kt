package com.goldenraccoon.mementomoricalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.goldenraccoon.mementomoricalendar.ui.theme.MementoMoriCalendarTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MementoMoriCalendarTheme {
                MainView(
                    navController = rememberNavController()
                )
            }
        }
    }
}
