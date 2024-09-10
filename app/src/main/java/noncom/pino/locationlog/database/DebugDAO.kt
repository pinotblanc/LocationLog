package noncom.pino.locationlog.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface DebugDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(entry: LocationLogEntry)

    @Query("SELECT (SELECT COUNT(*) FROM location) == 0")
    fun isEmpty(): Boolean

    @Query("SELECT * FROM location ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastLocation(): LocationLogEntry

    @Query("SELECT * FROM location ORDER BY timestamp DESC")
    suspend fun getLocationsNewestFirst(): List<LocationLogEntry>
}