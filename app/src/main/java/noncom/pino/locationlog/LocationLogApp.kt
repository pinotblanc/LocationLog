package noncom.pino.locationlog

import androidx.compose.runtime.Composable
import noncom.pino.locationlog.database.LocationLogEntry
import noncom.pino.locationlog.ui.Timeline


@Composable
fun LocationLogApp(db: List<LocationLogEntry>) {

    // TODO: create bottom navbar
    // TODO: create top bar (to cover notches -> WindowInsets)
    // TODO: create map screen

    Timeline(db)
}