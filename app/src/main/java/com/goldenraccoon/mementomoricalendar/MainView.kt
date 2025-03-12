package com.goldenraccoon.mementomoricalendar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.goldenraccoon.mementomoricalendar.ui.pages.BirthdayPage
import com.goldenraccoon.mementomoricalendar.ui.viewmodels.BirthdayViewModel

enum class AppRoutes() {
    Birthday,
    TotalWeeksGrid
}

@Composable
fun MainView() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = AppRoutes.Birthday.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = AppRoutes.Birthday.name) {
                BirthdayPage(
                    viewModel = hiltViewModel()
                )
            }
        }
    }
}