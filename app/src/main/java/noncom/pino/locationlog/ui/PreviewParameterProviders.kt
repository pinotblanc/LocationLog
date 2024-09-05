package noncom.pino.locationlog.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import noncom.pino.locationlog.database.LocationLogEntry

class DBProvider: PreviewParameterProvider<List<LocationLogEntry>> {

    override val values = sequenceOf(
        listOf(
            LocationLogEntry(1725373023815L, 40.730612346, -73.935242653),
            LocationLogEntry(1725375919625L, 40.730612346, -73.935242653),
            LocationLogEntry(1725376892450L, 40.730612346, -73.935242653),
            LocationLogEntry(1725379872213L, 40.730612346, -73.935242653),
            LocationLogEntry(1725383803618L, 40.730612346, -73.935242653),
            LocationLogEntry(1725385109589L, 40.730612346, -73.935242653),
            LocationLogEntry(1725386124281L, 40.730612346, -73.935242653),
            LocationLogEntry(1725387039807L, 40.730612346, -73.935242653),
            LocationLogEntry(1725387957509L, 40.730612346, -73.935242653),
            LocationLogEntry(1725388935244L, 40.730612346, -73.935242653),
            LocationLogEntry(1725389838195L, 40.730612346, -73.935242653),
            LocationLogEntry(1725391074420L, 40.730612346, -73.935242653),
            LocationLogEntry(1725393938380L, 40.730612346, -73.935242653),
            LocationLogEntry(1725395077312L, 40.730612346, -73.935242653),
            LocationLogEntry(1725396009313L, 40.730612346, -73.935242653),
            LocationLogEntry(1725397179546L, 40.730612346, -73.935242653),
            LocationLogEntry(1725398176153L, 40.730612346, -73.935242653),
            LocationLogEntry(1725399366007L, 40.730612346, -73.935242653),
            LocationLogEntry(1725400266105L, 40.730612346, -73.935242653),
            LocationLogEntry(1725401166142L, 40.730612346, -73.935242653),
            LocationLogEntry(1725402066141L, 40.730612346, -73.935242653),
            LocationLogEntry(1725403238506L, 40.730612346, -73.935242653),
            LocationLogEntry(1725404140676L, 40.730612346, -73.935242653)
        )
    )
}