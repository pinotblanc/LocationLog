package noncom.pino.locationlog.utils

import noncom.pino.locationlog.database.LocationLogEntry

class AppState(
    db: List<LocationLogEntry>,
    settings: Settings
) {
    var db = db
    var settings = settings
}