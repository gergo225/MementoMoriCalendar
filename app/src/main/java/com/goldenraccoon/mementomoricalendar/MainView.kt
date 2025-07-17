package com.goldenraccoon.mementomoricalendar

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.goldenraccoon.mementomoricalendar.ui.pages.SetupPage
import com.goldenraccoon.mementomoricalendar.ui.pages.SettingsPage
import com.goldenraccoon.mementomoricalendar.ui.pages.StatisticsPage
import com.goldenraccoon.mementomoricalendar.ui.pages.WeeksGridPage

enum class AppRoutes(val title: String) {
    Birthday(""),
    TotalWeeksGrid("MEMENTO MORI"),
    Settings("Settings"),
    Statistics("Your Life")
}

@Composable
fun MainView(
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            MainAppBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoutes.TotalWeeksGrid.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = AppRoutes.Birthday.name) {
                SetupPage(
                    onFinishClicked = {
                        navController.navigate(AppRoutes.TotalWeeksGrid.name) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = false
                                inclusive = true
                            }
                        }
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
                    onNavigateToSetupPage = {
                        navController.navigate(AppRoutes.Birthday.name)
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

            composable(
                route = AppRoutes.Statistics.name,
                enterTransition = {
                    slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        animationSpec = tween(500, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                StatisticsPage()
            }
        }
    }
}

@Composable
private fun MainAppBar(
    navController: NavHostController
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = AppRoutes.entries.firstOrNull { it.name == currentBackStackEntry?.destination?.route }
    val isStartingPage = currentRoute == AppRoutes.TotalWeeksGrid
    val canNavigateBack = navController.previousBackStackEntry != null && !isStartingPage
    val hasAppBar = currentRoute != AppRoutes.Birthday

    if (hasAppBar) {
        AppBar(
            currentRoute = currentRoute ?: AppRoutes.Birthday,
            canNavigateBack = canNavigateBack,
            navigateUp = { navController.navigateUp() },
            leadingButton = {
                if (isStartingPage) {
                    IconButton(
                        onClick = { navController.navigate(AppRoutes.Settings.name) }
                    ) {
                        Icon(painter = painterResource(R.drawable.discover_tune), contentDescription = "Settings")
                    }
                }
            },
            trailingButton = {
                if (isStartingPage) {
                    IconButton(
                        onClick = { navController.navigate(AppRoutes.Statistics.name) }
                    ) {
                        Icon(imageVector = Icons.Default.BarChart, contentDescription = "Statistics")
                    }
                }
            }
        )
    }
}