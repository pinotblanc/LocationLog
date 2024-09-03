package noncom.pino.locationlog.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface LocationLogDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(entry: LocationLogEntry)

    @Query("SELECT * FROM location ORDER BY timestamp DESC LIMIT 1")
    fun getLastLocation(): LocationLogEntry

    @Query("SELECT * FROM location ORDER BY timestamp DESC")
    fun getLocationsNewestFirst(): Flow<List<LocationLogEntry>>
}