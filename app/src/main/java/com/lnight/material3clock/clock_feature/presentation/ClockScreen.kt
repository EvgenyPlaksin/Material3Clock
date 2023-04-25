package com.lnight.material3clock.clock_feature.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.lnight.material3clock.core_ui.TitleSection
import com.lnight.material3clock.ui.theme.Material3ClockTheme

@Composable
fun ClockScreen() {
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
                titleText = "Alarm",
                titleShadow = 0.dp,
                titleSectionAlpha = ContentAlpha.medium,
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
                    offset = DpOffset(10.dp, 0.dp)
                ) {
                    DropdownMenuItem(onClick = { /*TODO*/ }, text = {
                        Text(text = "Screen saver")
                    })
                    DropdownMenuItem(onClick = { /*TODO*/ }, text = {
                        Text(text = "Settings")
                    })
                }
            }
        }
    }
}



@Preview
@Composable
fun ClockScreenPreview() {
    Material3ClockTheme {
        ClockScreen()
    }
}