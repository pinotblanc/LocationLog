package noncom.pino.locationlog.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import noncom.pino.locationlog.ui.StateProvider
import noncom.pino.locationlog.utils.AppState

@Preview
@Composable
fun SettingsScreen(@PreviewParameter(StateProvider::class) state: AppState) {

    // TODO debug
    Column(
        modifier = Modifier.background(Color.White).padding(top = 0.dp, bottom = 0.dp, start = 8.dp, end = 8.dp).fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "debug",
            color = Color.Black,
            fontSize = 50.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(top = 50.dp, bottom = 15.dp, start = 0.dp, end = 0.dp)
        )
        if (state.debug.isNotEmpty()) {

            // db table
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.debug) { entry -> TimelineEntry(entry) }
                item { Row(modifier = Modifier.height(8.dp)) {} }
            }
        }
        else {
            Text(
                text = buildAnnotatedString { withStyle(style = SpanStyle(fontFeatureSettings = "tnum")) { append("database is empty!") } },
                color = Color.Black,
                fontSize = TextStyle.Default.fontSize,
                fontWeight = FontWeight.Medium
            )
        }
    }

//    Box(
//        modifier = Modifier.fillMaxSize().background(Color.White),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = "settings go here",
//            color = Color.Black,
//            fontSize = TextStyle.Default.fontSize,
//            fontWeight = FontWeight.Bold
//        )
//    }
}