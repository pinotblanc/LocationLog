package noncom.pino.locationlog.utils

import android.content.Context
import noncom.pino.locationlog.database.LocationLogEntry

class AppState(
    db: List<LocationLogEntry>,
    settings: Settings,
    context: Context
) {
    var db = db
    var settings = settings
    var context = context
}