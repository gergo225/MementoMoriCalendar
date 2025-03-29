package com.goldenraccoon.mementomoricalendar

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.goldenraccoon.mementomoricalendar.ui.pages.BirthdayPage
import com.goldenraccoon.mementomoricalendar.ui.pages.SettingsPage
import com.goldenraccoon.mementomoricalendar.ui.pages.WeeksGridPage

enum class AppRoutes() {
    Birthday,
    TotalWeeksGrid,
    Settings
}

@Composable
fun MainView() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = AppRoutes.TotalWeeksGrid.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = AppRoutes.Birthday.name) {
                BirthdayPage(
                    onContinueClicked = {
                        navController.navigate(AppRoutes.TotalWeeksGrid.name)
                    }
                )
            }

            composable(
                route = AppRoutes.TotalWeeksGrid.name,
                enterTransition = {
                    fadeIn()
                },
                exitTransition = {
                    fadeOut()
                }
            ) {
                WeeksGridPage(
                    onNavigateToBirthdayPage = {
                        navController.navigate(AppRoutes.Birthday.name)
                    },
                    onNavigateToSettingsPage = {
                        navController.navigate(AppRoutes.Settings.name)
                    },
                    onNavigateToStatisticsPage = {
                        // TODO: navigate to statistics page
                    }
                )
            }

            composable(
                route = AppRoutes.Settings.name,
                enterTransition = {
                    slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        animationSpec = tween(500, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                }
            ) {
                SettingsPage(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}