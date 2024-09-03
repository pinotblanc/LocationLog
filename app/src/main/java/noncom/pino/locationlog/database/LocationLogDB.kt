package noncom.pino.locationlog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LocationLogEntry::class], version = 1, exportSchema = false)
abstract class LocationLogDB: RoomDatabase() {

        abstract fun dao(): LocationLogDAO

        companion object {

                @Volatile
                private var INSTANCE: LocationLogDB? = null

                fun getDatabase(context: Context): LocationLogDB {

                        // check whether DB is initialized
                        if (INSTANCE == null) {
                                synchronized(this) {
                                        INSTANCE = Room.databaseBuilder(context.applicationContext, LocationLogDB::class.java, "locations.db").build()
                                }
                        }
                        return INSTANCE as LocationLogDB
                }
        }
}