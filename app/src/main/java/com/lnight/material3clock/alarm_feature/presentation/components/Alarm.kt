package com.lnight.material3clock.alarm_feature.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Label
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lnight.material3clock.alarm_feature.presentation.AlarmStateItem
import com.lnight.material3clock.core.Day
import java.time.LocalDateTime
import java.util.*

@Composable
fun Alarm(
    item: AlarmStateItem,
    onLabelClick: (AlarmStateItem) -> Unit,
    onDeleteClick: (AlarmStateItem) -> Unit,
    onChangeTimeClick: (AlarmStateItem) -> Unit,
    onAlarmClick: (AlarmStateItem) -> Unit,
    onChangeIsActive: (AlarmStateItem) -> Unit,
    onRepeatDaysChange: (AlarmStateItem, List<Day>) -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor by animateColorAsState(
        if (item.isActive) MaterialTheme.colorScheme.onSurface else Color(0xFF858585)
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onAlarmClick(item) }
            .background(MaterialTheme.colorScheme.onSecondary)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (item.isDetailsVisible) {
                    if (item.label != null) {
                        Text(
                            text = item.label,
                            fontSize = 13.sp,
                            color = textColor
                        )
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onLabelClick(item) }
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
                                color = MaterialTheme.colorScheme.surfaceTint
                            )
                        }
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
                        .clickable { onAlarmClick(item) }
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "${item.dateTime.hour}:${item.dateTime.minute}",
                style = MaterialTheme.typography.headlineMedium,
                color = textColor,
                fontWeight = if (item.isDetailsVisible) FontWeight.Bold else FontWeight.ExtraBold,
                modifier = Modifier.clickable { onChangeTimeClick(item) }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                var scheduledText by remember {
                    mutableStateOf("")
                }
                scheduledText = if (item.nextDay == null && !item.isActive) {
                    "Not scheduled"
                } else if (item.isActive) {
                    "Today"
                } else if (item.nextDay?.name == LocalDateTime.now().plusDays(1).dayOfWeek.name) {
                    "Tomorrow"
                } else {
                    item.nextDay?.name?.lowercase()?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.getDefault()
                        ) else it.toString()
                    } ?: "Unknown"
                }
                Text(
                    text = scheduledText,
                    fontSize = 13.sp,
                    color = textColor
                )
                Switch(
                    checked = item.isActive,
                    onCheckedChange = { onChangeIsActive(item) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.secondary,
                        checkedTrackColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
                AnimatedVisibility(
                    visible = item.isDetailsVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Column {

                        LazyRow(
                            modifier = Modifier.fillMaxWidth()
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
                                            val newList = mutableListOf<Day>()
                                            item.repeatDays.forEach { day ->
                                                if (it != day) newList.add(day)
                                            }
                                            onRepeatDaysChange(item, newList)
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
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(
                            modifier = Modifier.clickable { onDeleteClick(item) },
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