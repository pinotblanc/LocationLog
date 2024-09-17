package noncom.pino.locationlog.utils


class Settings(timeframe: Timeframe) {

    var timeframe = timeframe
}

enum class Timeframe { TODAY, YESTERDAY, LAST_3DAYS, LAST_5DAYS, LAST_WEEK, ALL }