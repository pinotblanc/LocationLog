package noncom.pino.locationlog.utils

import noncom.pino.locationlog.database.LocationLogEntry

class AppState(
    db: List<LocationLogEntry>,
    debug: List<LocationLogEntry>,
    settings: Settings
) {
    var db = db
    var debug = debug
    var settings = settings
}