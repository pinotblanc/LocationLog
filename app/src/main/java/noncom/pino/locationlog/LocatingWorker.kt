package noncom.pino.locationlog

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

                Log.d("Location", "${entry.latitude} : ${entry.longitude} at ${Timestamp(entry.timestamp)}")

                // TODO: check whether last location is within some delta value
                CoroutineScope(Dispatchers.IO).launch { try { dao.insertLocation(entry) } catch(e: Exception) { Log.d("Location", e.toString()) } }
            }
            else { Log.d("Location", "failed to fetch location") }
        }
        try { locationClient.lastLocation.addOnCompleteListener(listener) }
        catch (e: SecurityException) { Log.e("Location", "lost location permission") }

        return Result.success()
    }
}