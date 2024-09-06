package noncom.pino.locationlog.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import noncom.pino.locationlog.database.LocationLogDB
import noncom.pino.locationlog.database.LocationLogEntry
import java.sql.Timestamp
import kotlin.math.abs
import kotlin.math.max


class LocatingWorker(private val context: Context, private val workerParams: WorkerParameters): CoroutineWorker(context, workerParams) {

    private lateinit var lastLocation: Location
    private val dao = LocationLogDB.getDatabase(applicationContext).dao()

    // fetch location and write to disk
    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {

        // instantiating components needed to fetch current location
        val locationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val locationCallback = object: LocationCallback() {}
        val listener = OnCompleteListener { task ->

            if (task.isSuccessful && task.result != null) {

                lastLocation = task.result
                locationClient.removeLocationUpdates(locationCallback)

                val entry = LocationLogEntry(System.currentTimeMillis(), lastLocation.latitude, lastLocation.longitude)

                // only stores entry if location is at least delta far away
                CoroutineScope(Dispatchers.IO).launch {

                    val delta = 0.00001
                    val lastLocation = dao.getLastLocation()
                    val diff = max(abs(lastLocation.latitude - entry.latitude), abs(lastLocation.longitude - entry.longitude))

                    // TODO: switch for (diff >= delta) and test if it works
                    if (true) {
                        dao.insertLocation(entry)
                        Log.d("Location", "STORED: ${entry.latitude} : ${entry.longitude} at ${Timestamp(entry.timestamp)}")

                    }
                    else {
                        Log.d("Location", "NOT STORED (diff=${diff}): ${entry.latitude} : ${entry.longitude} at ${Timestamp(entry.timestamp)}")
                    }
                }
            }
            else { Log.d("Location", "failed to fetch location") }
        }
        try { locationClient.lastLocation.addOnCompleteListener(listener) }
        catch (e: SecurityException) { Log.e("Location", "lost location permission") }

        return Result.success()
    }
}