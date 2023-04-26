package com.lnight.material3clock.clock_feature.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lnight.material3clock.R

@Composable
fun ClockText(
    modifier: Modifier = Modifier,
    formattedTime: String,
    formattedDate: String,
    nextAlarm: String? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 72.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formattedDate
            )
            Spacer(modifier = Modifier.width(2.dp))
            if(nextAlarm != null) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_alarm),
                    contentDescription = "Alarm",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = nextAlarm
                )
            }
        }
    }
}