package com.lnight.material3clock.alarm_feature.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lnight.material3clock.alarm_feature.presentation.AlarmStateItem
import com.lnight.material3clock.core.Day
import com.lnight.material3clock.ui.theme.Material3ClockTheme
import com.marosseleng.compose.material3.datetimepickers.time.domain.noSeconds

@Composable
fun Alarm(
    item: AlarmStateItem,
    scheduledText: String,
    onLabelClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onChangeTimeClick: () -> Unit,
    onAlarmClick: () -> Unit,
    onChangeIsActive: () -> Unit,
    onChangeVibrateClick: () -> Unit,
    onRepeatDaysChange: (List<Day>) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current

    val textAlpha by animateFloatAsState(
        if (item.isActive) ContentAlpha.high else ContentAlpha.disabled
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onAlarmClick() }
            .background(if(isSystemInDarkTheme()) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (item.label != null) {
                    Text(
                        text = item.label,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .alpha(textAlpha)
                            .clickable { onLabelClick() }
                    )
                } else if (item.isDetailsVisible) {
                    Row(
                        modifier = Modifier
                            .clickable { onLabelClick() },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Label,
                            contentDescription = "Add Label",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Add label",
                            fontSize = 13.sp,
                            color = Color(0xFF858585)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(20.dp))
                }
                Icon(
                    imageVector = if (item.isDetailsVisible) Icons.Default.ArrowCircleUp else Icons.Default.ArrowCircleDown,
                    contentDescription = if (item.isDetailsVisible) "Hide details section" else "Show details section",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onAlarmClick() }
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "${item.dateTime.toLocalTime().noSeconds()}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight(if (item.isActive) 800 else 500),
                modifier = Modifier
                    .clickable { onChangeTimeClick() }
                    .alpha(textAlpha)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = scheduledText,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.alpha(textAlpha)
                )
                Switch(
                    checked = item.isActive,
                    onCheckedChange = { onChangeIsActive() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.secondary,
                        checkedTrackColor = MaterialTheme.colorScheme.onPrimary,
                        uncheckedBorderColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    thumbContent = {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary)
                                .size(30.dp)
                        )
                    }
                )
            }
            AnimatedVisibility(
                visible = item.isDetailsVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Column {
                    val screenWidth = configuration.screenWidthDp.dp.value
                    val padding = (screenWidth - 52f) / 7f - 33f
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        items(Day.values()) {
                            Box(
                                modifier = Modifier
                                    .size(35.dp)
                                    .border(
                                        color = if (item.repeatDays.contains(it)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                        width = 1.dp,
                                        shape = CircleShape
                                    )
                                    .clip(CircleShape)
                                    .background(if (item.repeatDays.contains(it)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSecondary)
                                    .clickable {
                                        if (item.repeatDays.contains(it)) {
                                            val newList = mutableListOf<Day>()
                                            item.repeatDays.forEach { repeatDay ->
                                                if (it != repeatDay) newList.add(repeatDay)
                                            }
                                            onRepeatDaysChange(newList)
                                        } else {
                                            val newList = mutableListOf<Day>()
                                            item.repeatDays.forEach { repeatDay ->
                                                newList.add(repeatDay)
                                            }
                                            newList.add(it)
                                            newList.sort()
                                            onRepeatDaysChange(newList)
                                        }
                                    }
                                    .padding(5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = it.name.toList().first().toString(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (item.repeatDays.contains(it)) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            if(it != Day.SATURDAY) {
                                Spacer(modifier = Modifier.width(padding.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .clickable { onChangeVibrateClick() }
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Vibration,
                                contentDescription = "Delete Alarm",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Vibrate",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                         IconButton(
                             onClick = onChangeVibrateClick,
                             modifier = Modifier
                                 .clip(CircleShape)
                                 .border(
                                     width = 2.dp,
                                     color = if (item.shouldVibrate) MaterialTheme.colorScheme.primary else Color(
                                         0xFF858585
                                     ),
                                     shape = CircleShape
                                 )
                                 .size(20.dp)
                                 .background(
                                     color = if (item.shouldVibrate) MaterialTheme.colorScheme.primary else Color.Transparent
                                 )
                                 .padding(1.dp)
                         ) {
                             if(item.shouldVibrate) {
                                 Icon(
                                     tint = MaterialTheme.colorScheme.onPrimary,
                                     imageVector = Icons.Filled.Check,
                                     contentDescription = "Turn off vibration",
                                     modifier = Modifier.fillMaxSize()
                                 )
                             }
                         }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.clickable { onDeleteClick() },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Alarm",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Delete",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AlarmPreview() {
    Material3ClockTheme {
        Alarm(
            item = AlarmStateItem(isDetailsVisible = true, shouldVibrate = true),
            scheduledText = "Scheduled text",
            {},
            {},
            {},
            {},
            {},
            {},
            {}
        )
    }
}