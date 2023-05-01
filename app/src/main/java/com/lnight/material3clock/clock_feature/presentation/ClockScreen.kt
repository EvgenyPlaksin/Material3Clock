package com.lnight.material3clock.clock_feature.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lnight.material3clock.clock_feature.presentation.components.ClockText
import com.lnight.material3clock.core_ui.TitleSection
import com.lnight.material3clock.ui.theme.Material3ClockTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ClockScreen(
    state: ClockState,
    navController: NavHostController,
    uiEvent: Flow<UiEvent>,
    onEvent: (ClockScreenEvent) -> Unit
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    navController.navigate(event.route)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var showMenu by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TitleSection(
                titleText = "Clock",
                titleShadow = 0.dp,
                titleSectionAlpha = ContentAlpha.high,
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(top = 38.dp)
            ) {
                IconButton(
                    onClick = { showMenu = !showMenu }
                ) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More")
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    offset = DpOffset(10.dp, 0.dp),
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    DropdownMenuItem(onClick = { onEvent(ClockScreenEvent.OnScreenSaverClick) }, text = {
                        Text(
                            text = "Screen saver",
                            fontSize = 16.sp
                        )
                    })
                    DropdownMenuItem(onClick = { /*TODO*/ }, text = {
                        Text(
                            text = "Settings",
                            fontSize = 16.sp
                        )
                    })
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        ClockText(
            formattedTime = state.formattedTime,
            formattedDate = state.formattedDate,
            nextAlarm = state.nextAlarm,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}



@Preview(showBackground = true)
@Composable
fun ClockScreenPreview() {
    Material3ClockTheme {
        ClockScreen(
            state = ClockState(),
            navController = rememberNavController(),
            uiEvent = MutableStateFlow(UiEvent.Navigate("test")),
            onEvent = {}
        )
    }
}