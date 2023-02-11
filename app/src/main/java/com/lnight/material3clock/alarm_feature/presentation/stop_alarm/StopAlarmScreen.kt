package com.lnight.material3clock.alarm_feature.presentation.stop_alarm

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.lnight.material3clock.R
import com.lnight.material3clock.alarm_feature.receivers.AlarmReceiver.Companion.shouldUpdateState
import com.lnight.material3clock.core.activity
import com.marosseleng.compose.material3.datetimepickers.time.domain.noSeconds
import java.io.IOException
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

@Composable
fun StopAlarmScreen(
    id: Int,
    navController: NavController,
    viewModel: StopAlarmViewModel = hiltViewModel()
) {
    val item = viewModel.state.item

    val context = LocalContext.current
    val time = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(item?.timestamp ?: 0),
        TimeZone.getDefault().toZoneId()
    ).toLocalTime().noSeconds()

    LaunchedEffect(key1 = true) {
        val sound = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.alarm_sound)
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        mediaPlayer.isLooping = true
        try {
            mediaPlayer.setDataSource(context, sound)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        viewModel.onEvent(AlarmStopEvent.PassId(id))

        viewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.NavigateUp -> {
                    shouldUpdateState = true
                    mediaPlayer.stop()
                    if (!navController.navigateUp()) {
                        context.activity?.finish()
                    }
                }
                is UiEvent.Navigate -> {
                    shouldUpdateState = true
                    mediaPlayer.stop()
                    navController.navigate(event.route)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = time.toString(),
            fontSize = 100.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = item?.label ?: "Alarm",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(200.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Button(
                onClick = { if (item != null) viewModel.onEvent(AlarmStopEvent.Snooze(item)) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier
                    .size(height = 70.dp, width = 150.dp)
                    .clip(RoundedCornerShape(30.dp))
            ) {
                Text(
                    text = "Snooze",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
            Button(
                onClick = { if (item != null) viewModel.onEvent(AlarmStopEvent.Stop(item)) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .size(height = 70.dp, width = 150.dp)
                    .clip(RoundedCornerShape(30.dp))
            ) {
                Text(
                    text = "Stop",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}