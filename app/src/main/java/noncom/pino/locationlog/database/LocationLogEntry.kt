package noncom.pino.locationlog.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "location")
data class LocationLogEntry(

    @PrimaryKey
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double
)