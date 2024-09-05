package noncom.pino.locationlog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import noncom.pino.locationlog.ui.StateProvider
import noncom.pino.locationlog.utils.AppState

@Preview
@Composable
fun SettingsScreen(@PreviewParameter(StateProvider::class) state: AppState) {

    // temp
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "timezone: ${state.settings.timezone}",
            color = Color.Black,
            fontSize = TextStyle.Default.fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}