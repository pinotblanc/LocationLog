package noncom.pino.locationlog.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import noncom.pino.locationlog.database.LocationLogEntry
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Preview
@Composable
fun Timeline(@PreviewParameter(DBProvider::class) db: List<LocationLogEntry>) {

    Column(
        modifier = Modifier.background(Color.White).padding(8.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: dynamic alignment in TimelineHeadline() and TimelineEntryHeadline()
        TimelineHeadline()
        TimelineEntryHeadline()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.drawBehind {
                drawRoundRect(Color(0xFFD3D3D3), cornerRadius = CornerRadius(10.dp.toPx()))
            }.padding(4.dp)
        ) {
            items(db) { entry -> TimelineEntry(entry) }
        }
    }
}

@Composable
fun TimelineHeadline() { Text(text = "timeline", color = Color.Black, fontSize = 30.sp) }

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun TimelineEntryHeadline() {

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(2.dp)
    ) {
        Text(modifier = Modifier
            .padding(1.dp)
            .size(100.dp, 18.dp),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontFeatureSettings = "tnum")) {
                    append("timestamp")
                }
            }
        )
        Text(modifier = Modifier
            .padding(1.dp)
            .size(70.dp, 18.dp),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontFeatureSettings = "tnum")) {
                    append("latitude")
                }
            }
        )
        Text(modifier = Modifier
            .padding(1.dp)
            .size(70.dp, 18.dp),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontFeatureSettings = "tnum")) {
                    append("longitude")
                }
            }
        )
    }
}

@Composable
fun TimelineEntry(entry: LocationLogEntry) {

    // convert timestamp to readable format
    val instant = Instant.ofEpochMilli(entry.timestamp)
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"))
    val formatter = DateTimeFormatter.ofPattern("dd-MMM HH:mm:")
    val formattedDateTime = localDateTime.format(formatter)
    // convert latitude and longitude to be accurate to approx 1m
    var latitude = entry.latitude.toString()
    latitude = latitude.substring(0, latitude.indexOf('.')+6)
    var longitude = entry.longitude.toString()
    longitude = longitude.substring(0, longitude.indexOf('.')+6)

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.drawBehind {
            drawRoundRect(Color(0xFFE5E4E2), cornerRadius = CornerRadius(10.dp.toPx()))
        }.padding(4.dp)
    ) {
        Text(modifier = Modifier
            .padding(1.dp)
            .size(100.dp, 18.dp),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontFeatureSettings = "tnum")) {
                    append(formattedDateTime)
                }
            }
        )
        Text(modifier = Modifier
            .padding(1.dp)
            .size(70.dp, 18.dp),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontFeatureSettings = "tnum")) {
                    append(latitude)
                }
            }
        )
        Text(modifier = Modifier
            .padding(1.dp)
            .size(70.dp, 18.dp),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontFeatureSettings = "tnum")) {
                    append(longitude)
                }
            }
        )
    }
}