package com.lnight.material3clock.core_ui

import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.lnight.material3clock.alarm_feature.presentation.AlarmScreen
import com.lnight.material3clock.alarm_feature.presentation.AlarmViewModel
import com.lnight.material3clock.alarm_feature.presentation.stop_alarm.StopAlarmScreen
import com.lnight.material3clock.alarm_feature.presentation.stop_alarm.StopAlarmViewModel
import com.lnight.material3clock.clock_feature.presentation.ClockScreen
import com.lnight.material3clock.core.BottomNavItem
import com.lnight.material3clock.core.Route

@Composable
fun NavGraph(navController: NavHostController, paddingValues: PaddingValues = PaddingValues()) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Alarm.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(BottomNavItem.Alarm.route) {
                val viewModel = hiltViewModel<AlarmViewModel>()
                AlarmScreen(
                    state = viewModel.state,
                    uiEvent = viewModel.uiEvent,
                    onEvent = viewModel::onEvent
                )
            }
            composable(
                Route.CancelAlarmScreen.route + "/{id}",
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "https://alarm.com/id={id}"
                        action = Intent.ACTION_VIEW
                    }
                ),
                arguments = listOf(
                    navArgument("id") {
                        type = NavType.IntType
                        defaultValue = -1
                    }
                )
            ) {
                val id = it.arguments?.getInt("id") ?: return@composable
                val viewModel = hiltViewModel<StopAlarmViewModel>()

                StopAlarmScreen(
                    id = id,
                    navController = navController,
                    state = viewModel.state,
                    uiEvent = viewModel.uiEvent,
                    onEvent = viewModel::onEvent
                )
            }
        composable(BottomNavItem.Clock.route) {
            ClockScreen()
        }
    }
}