package noncom.pino.locationlog.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import noncom.pino.locationlog.utils.ActivityListenerService
import noncom.pino.locationlog.utils.AppState
import noncom.pino.locationlog.utils.Timeframe

@Composable
fun SettingsScreen(state: AppState) {

    Column(
        modifier = Modifier.padding(top = 50.dp, bottom = 0.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        SettingsHeadline()
        TrackingSetting(state)
        TimeframeSetting(state)
    }
}

@Composable
fun SettingsHeadline() {

    Text(
        text = "Settings",
        color = Color.Black,
        fontSize = 50.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier.padding(top = 0.dp, bottom = 15.dp, start = 0.dp, end = 0.dp)
    )
}

@Composable
fun TrackingSetting(state: AppState) {

    Column (modifier = Modifier.padding(top = 0.dp, bottom = 8.dp, start = 0.dp, end = 0.dp)) {

        Text(
            text = "location tracking",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 0.dp, bottom = 8.dp, start = 0.dp, end = 0.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(35.dp)
                .fillMaxSize()
        ) {
            Button(
                onClick = {
                    Intent(state.context, ActivityListenerService::class.java).also {

                        it.action = ActivityListenerService.Actions.START.toString()
                        state.context.startService(it)
                    }
                }
            ) { Text(text = "start") }

            Button(
                onClick = {
                    Intent(state.context, ActivityListenerService::class.java).also {

                        it.action = ActivityListenerService.Actions.STOP.toString()
                        state.context.startService(it)
                    }
                }
            ) { Text(text = "stop") }
        }
    }
}

@Composable
fun TimeframeSetting(state: AppState) {

    Column (modifier = Modifier.padding(top = 0.dp, bottom = 8.dp, start = 0.dp, end = 0.dp)) {

        var text by remember { mutableStateOf("debug") }
        if (state.settings.timeframe == Timeframe.ALL) text = "all"
        if (state.settings.timeframe == Timeframe.TODAY) text = "today"

        Text(
            text = "map timeframe",
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 0.dp, bottom = 8.dp, start = 0.dp, end = 0.dp)
        )

        Button(
            onClick = {
                if (state.settings.timeframe == Timeframe.ALL) {

                    text = "today"
                    state.settings.timeframe = Timeframe.TODAY
                }
                if (state.settings.timeframe == Timeframe.TODAY) {

                    text = "all"
                    state.settings.timeframe = Timeframe.ALL
                }
            }
        ) { Text(text = text) }
    }
}