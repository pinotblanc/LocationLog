package noncom.pino.locationlog.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import noncom.pino.locationlog.database.LocationLogEntry
import noncom.pino.locationlog.utils.AppState
import noncom.pino.locationlog.utils.Timeframe
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

@Composable
fun MapScreen(state: AppState) {

    Column(
        modifier = Modifier
            .padding(top = 50.dp, bottom = 0.dp, start = 0.dp, end = 0.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        MapHeadline()
        Map(state)
    }
}

@Composable
fun MapHeadline() {

    Text(
        text = "Map",
        color = Color.Black,
        fontSize = 50.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier.padding(top = 0.dp, bottom = 15.dp, start = 8.dp, end = 0.dp)
    )
}

@Composable
fun Map(state: AppState) {

    val mapProperties = MapProperties(isMyLocationEnabled = true)
    var cameraPositionState = rememberCameraPositionState()

    if (state.db.isNotEmpty()) {

        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(state.db[0].latitude, state.db[0].longitude), 15f)
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = mapProperties,
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(zoomControlsEnabled = false, indoorLevelPickerEnabled = false),
        contentPadding = PaddingValues(bottom = 2000000000.dp) // so water mark is off screen
    ) {
        if (state.db.isNotEmpty()) {

            var entries = mutableListOf<LocationLogEntry>()

            // fill entries list depending on setting
            if (state.settings.timeframe == Timeframe.ALL) { entries = state.db.toMutableList() }

            else if (state.settings.timeframe == Timeframe.TODAY) {

                // get formater and todays date
                val instant = Instant.ofEpochMilli(System.currentTimeMillis())
                val localDateTime = LocalDateTime.ofInstant(instant, TimeZone.getDefault().toZoneId())
                val formatter = DateTimeFormatter.ofPattern("MMM dd")
                val today = localDateTime.format(formatter)

                for (entry in state.db) {

                    if(today != LocalDateTime.ofInstant(Instant.ofEpochMilli(entry.timestamp), TimeZone.getDefault().toZoneId()).format(formatter)) break
                    entries.add(entry)
                }
            }
            // TODO other cases

            // actual line with the selected data
            Polyline(
                points = entries.map { entry -> LatLng(entry.latitude, entry.longitude) },
                clickable = true,
                color = Color.Blue,
                width = 10f
            )
        }
    }
}