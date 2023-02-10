package com.lnight.material3clock

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem
import com.lnight.material3clock.alarm_feature.presentation.AlarmScreen
import com.lnight.material3clock.alarm_feature.presentation.StopAlarmScreen
import com.lnight.material3clock.core.Route
import com.lnight.material3clock.ui.theme.Material3ClockTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
        setContent {
            val navController = rememberNavController()
            Material3ClockTheme {
                    val systemUiController = rememberSystemUiController()
                    val darkColors = !isSystemInDarkTheme()

                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            Color.Transparent,
                            darkColors
                        )
                    }
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                       NavHost(navController = navController, startDestination = Route.AlarmScreen.route) {
                           composable(Route.AlarmScreen.route) {
                               AlarmScreen()
                           }
                           composable(
                               Route.CancelAlarmScreen.route + "/{json}",
                               deepLinks = listOf(
                                   navDeepLink {
                                       uriPattern = "https://alarm.com/{json}"
                                       action = Intent.ACTION_VIEW
                                   }
                               ),
                               arguments = listOf(
                                   navArgument("json") {
                                       type = NavType.StringType
                                   }
                               )
                           ) {
                               val json = it.arguments?.getString("json") ?: return@composable
                               val item = Json.decodeFromString<AlarmItem>(json)
                               StopAlarmScreen(item)
                           }
                       }
                    }
            }
        }
    }
}
