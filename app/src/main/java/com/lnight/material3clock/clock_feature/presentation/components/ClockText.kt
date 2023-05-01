package com.lnight.material3clock.clock_feature.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lnight.material3clock.R

@Composable
fun ClockText(
    modifier: Modifier = Modifier,
    formattedTime: String,
    formattedDate: String,
    nextAlarm: String? = null,
    isCenter: Boolean = false,
    textColor: Color = Color.Unspecified
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = if(isCenter) Alignment.CenterHorizontally else Alignment.Start
    ) {
        Text(
            text = formattedTime,
            style = MaterialTheme.typography.headlineLarge,
            fontSize = 72.sp,
            color = textColor
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formattedDate,
                modifier = Modifier.padding(start = 5.dp),
                color = textColor
            )
            Spacer(modifier = Modifier.width(2.dp))
            if(nextAlarm != null) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_alarm),
                    contentDescription = "Alarm",
                    modifier = Modifier.size(16.dp),
                    tint = textColor
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = nextAlarm,
                    color = textColor
                )
            }
        }
    }
}