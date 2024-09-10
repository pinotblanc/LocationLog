package noncom.pino.locationlog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LocationLogEntry::class], version = 1, exportSchema = false)
abstract class DebugDB: RoomDatabase() {

    abstract fun dao(): DebugDAO

    companion object {

        @Volatile
        private var INSTANCE: DebugDB? = null

        fun getDatabase(context: Context): DebugDB {

            // check whether DB is initialized
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, DebugDB::class.java, "debug.db").build()
                }
            }
            return INSTANCE as DebugDB
        }
    }
}