package noncom.pino.locationlog.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import noncom.pino.locationlog.database.LocationLogEntry
import noncom.pino.locationlog.ui.StateProvider
import noncom.pino.locationlog.utils.AppState

@Preview
@Composable
fun MapScreen(@PreviewParameter(StateProvider::class) state: AppState) {

    Column(
        modifier = Modifier.background(Color.White).fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Map",
            color = Color.Black,
            fontSize = 50.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(top = 50.dp, bottom = 15.dp, start = 8.dp, end = 0.dp)
        )
        Map(state.db)
    }
}

@Composable
fun Map(db: List<LocationLogEntry>) {

    val mapProperties = MapProperties(isMyLocationEnabled = true)
    var cameraPositionState = rememberCameraPositionState()

    if (db.isNotEmpty()) {

        cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(db[0].latitude, db[0].longitude), 15f)
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = mapProperties,
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(zoomControlsEnabled = false, indoorLevelPickerEnabled = false),
        contentPadding = PaddingValues(bottom = 2000000000.dp) // so water mark is off screen
    ) {
        if (db.isNotEmpty()) {

            Polyline(
                points = db.map { entry -> LatLng(entry.latitude, entry.longitude) },
                clickable = true,
                color = Color.Blue,
                width = 10f
            )
        }
    }
}