package noncom.pino.locationlog.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import noncom.pino.locationlog.database.LocationLogEntry
import java.sql.Timestamp

@Preview
@Composable
fun Timeline(@PreviewParameter(DBProvider::class) db: List<LocationLogEntry>) {

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.White)
            .padding(4.dp)
        ) {

        TimelineHeadline()

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "timestamp", modifier = Modifier.padding(1.dp))
            Text(text = "latitude", modifier = Modifier.padding(1.dp))
            Text(text = "longitude", modifier = Modifier.padding(1.dp))
        }

        for (entry in db) TimelineEntry(entry)
    }
}

@Composable
fun TimelineHeadline() { Text(text = "timeline", color = Color.Black, fontSize = 30.sp) }

@Composable
fun TimelineEntry(entry: LocationLogEntry) {

    var timestamp = Timestamp(entry.timestamp).toString()
    timestamp = timestamp.substring(0, 19)

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = timestamp, modifier = Modifier.padding(1.dp))
        Text(text = entry.latitude.toString(), modifier = Modifier.padding(1.dp))
        Text(text = entry.longitude.toString(), modifier = Modifier.padding(1.dp))
    }
}

class DBProvider: PreviewParameterProvider<List<LocationLogEntry>> {

    override val values = sequenceOf(
        listOf(
            LocationLogEntry(0L, 40.730610, -73.935242),
            LocationLogEntry(0L, 40.730610, -73.935242),
            LocationLogEntry(0L, 40.730610, -73.935242),
            LocationLogEntry(0L, 40.730610, -73.935242),
            LocationLogEntry(0L, 40.730610, -73.935242),
            LocationLogEntry(0L, 40.730610, -73.935242),
            LocationLogEntry(0L, 40.730610, -73.935242)
        )
    )
}