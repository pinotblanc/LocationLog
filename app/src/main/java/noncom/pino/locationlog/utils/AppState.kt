package noncom.pino.locationlog.utils

import noncom.pino.locationlog.database.LocationLogEntry

class AppState(
    db: List<LocationLogEntry>,
    debug: List<LocationLogEntry>, // TODO debug
    settings: Settings
) {
    var db = db
    var debug = debug // TODO debug
    var settings = settings
}