package noncom.pino.locationlog.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import noncom.pino.locationlog.database.LocationLogEntry

@Preview
@Composable
fun Map(@PreviewParameter(DBProvider::class) db: List<LocationLogEntry>) {


}
