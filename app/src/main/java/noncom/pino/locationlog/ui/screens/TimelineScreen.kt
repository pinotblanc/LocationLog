package noncom.pino.locationlog.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import noncom.pino.locationlog.database.LocationLogEntry
import noncom.pino.locationlog.utils.AppState
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone
import kotlin.math.min


@Composable
fun TimelineScreen(state: AppState) {

    Column(
        modifier = Modifier.padding(top = 50.dp, bottom = 0.dp, start = 16.dp, end = 16.dp).fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        TimelineHeadline()

        if (state.db.isNotEmpty()) Timeline(state)
        else {
            Text(
                text = buildAnnotatedString { withStyle(style = SpanStyle(fontFeatureSettings = "tnum")) { append("database is empty!") } },
                color = Color.Black,
                fontSize = TextStyle.Default.fontSize,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun Timeline(state: AppState) {

    // db table
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.padding(top = 0.dp, bottom = 0.dp, start = 8.dp, end = 8.dp).fillMaxSize()
    ) {
        items(state.db) { entry -> TimelineEntry(entry) }
        item { Row(modifier = Modifier.height(8.dp)) {} }
    }
}

@Composable
fun TimelineHeadline() {

    Text(
        text = "Timeline",
        color = Color.Black,
        fontSize = 50.sp,
        fontWeight = FontWeight.Normal,
        modifier = Modifier.padding(top = 0.dp, bottom = 15.dp, start = 0.dp, end = 0.dp)
    )
}

@Composable
fun TimelineEntry(entry: LocationLogEntry) {

    // convert timestamp to readable format
    val instant = Instant.ofEpochMilli(entry.timestamp)
    val localDateTime = LocalDateTime.ofInstant(instant, TimeZone.getDefault().toZoneId())
    val formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm")
    val formattedDateTime = localDateTime.format(formatter)
    // convert latitude and longitude to be accurate to approx 1m
    var latitude = entry.latitude.toString()
    latitude = latitude.substring(0, min(latitude.indexOf('.')+6, latitude.length-1))
    var longitude = entry.longitude.toString()
    longitude = longitude.substring(0, min(longitude.indexOf('.')+6, longitude.length-1))

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .drawBehind {
                drawRoundRect(Color(0xFFE8E8E8), cornerRadius = CornerRadius(10.dp.toPx()))
            }
            .padding(4.dp)
            .height(30.dp)
            .fillMaxSize()
    ) {
        Text(
            text = buildAnnotatedString { withStyle(style = SpanStyle(fontFeatureSettings = "tnum")) { append(formattedDateTime) } },
            color = Color.Black,
            fontSize = TextStyle.Default.fontSize,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = buildAnnotatedString { withStyle(style = SpanStyle(fontFeatureSettings = "tnum")) { append("$latitude : $longitude") } },
            color = Color.Black,
            fontSize = TextStyle.Default.fontSize,
            fontWeight = FontWeight.Medium
        )

    }
}