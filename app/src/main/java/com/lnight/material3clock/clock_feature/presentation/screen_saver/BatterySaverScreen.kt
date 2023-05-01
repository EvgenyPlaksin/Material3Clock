package com.lnight.material3clock.clock_feature.presentation.screen_saver

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lnight.material3clock.clock_feature.presentation.components.ClockText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun BatterySaverScreen(
    state: BatterySaverState,
    navController: NavHostController,
    shouldShowBottomNav: MutableState<Boolean>,
    uiEvent: Flow<UiEvent>,
    onEvent: (BatterySaverScreenEvent) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = MutableInteractionSource()
            ) { onEvent(BatterySaverScreenEvent.OnSurfaceClick) },
        color = Color.Black
    ) {
        var isOnCenter by rememberSaveable {
            mutableStateOf(false)
        }

        val animatedStartClockBackground by animateColorAsState(
            if (!isOnCenter) Color(0xFFF8F8F8) else Color.Black,
            tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
        )

        val animatedCenterClockBackground by animateColorAsState(
            if (isOnCenter) Color(0xFFF8F8F8) else Color.Black,
            tween(delayMillis = 1100, durationMillis = 1000, easing = LinearOutSlowInEasing)
        )

        LaunchedEffect(key1 = true) {
            uiEvent.collect { event ->
                when (event) {
                    UiEvent.NavigateUp -> {
                        shouldShowBottomNav.value = true
                        navController.navigateUp()
                    }
                }
            }
        }

        BackHandler(true) {
            onEvent(BatterySaverScreenEvent.OnSurfaceClick)
        }

        LaunchedEffect(key1 = true) {
            shouldShowBottomNav.value = false

            var counter = 0
            while (counter < 30) {
                counter++
                delay(1000L)
            }
            isOnCenter = true
        }
        ClockText(
            formattedTime = state.formattedTime,
            formattedDate = state.formattedDate,
            nextAlarm = state.nextAlarm,
            textColor = animatedStartClockBackground,
            modifier = Modifier
                .padding(top = 12.dp, start = 12.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ClockText(
                formattedTime = state.formattedTime,
                formattedDate = state.formattedDate,
                nextAlarm = state.nextAlarm,
                textColor = animatedCenterClockBackground,
                isCenter = true,
                modifier = Modifier
                    .padding(bottom = 250.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BatterySaverScreenPreview() {
    val testState = remember { mutableStateOf(false) }

    BatterySaverScreen(
        state = BatterySaverState(),
        rememberNavController(),
        testState,
        uiEvent = MutableStateFlow(UiEvent.NavigateUp),
        onEvent = {}
    )
}