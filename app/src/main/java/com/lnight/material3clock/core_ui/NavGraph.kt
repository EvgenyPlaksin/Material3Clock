package com.lnight.material3clock.core_ui

import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.lnight.material3clock.alarm_feature.presentation.AlarmScreen
import com.lnight.material3clock.alarm_feature.presentation.stop_alarm.StopAlarmScreen
import com.lnight.material3clock.clock_feature.presentation.ClockScreen
import com.lnight.material3clock.core.BottomNavItem
import com.lnight.material3clock.core.Route

@Composable
fun NavGraph(navController: NavHostController, paddingValues: PaddingValues = PaddingValues()) {
    NavHost(
        navController = navController,
        startDestination = Route.AlarmScreen.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        navigation(startDestination = BottomNavItem.Alarm.route, route = Route.AlarmScreen.route) {
            composable(BottomNavItem.Alarm.route) {
                AlarmScreen()
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
            ) childComposable@ {
                val id = it.arguments?.getInt("id") ?: return@childComposable
                StopAlarmScreen(
                    id = id,
                    navController = navController
                )
            }
        }
        composable(BottomNavItem.Clock.route) {
            ClockScreen()
        }
    }
}