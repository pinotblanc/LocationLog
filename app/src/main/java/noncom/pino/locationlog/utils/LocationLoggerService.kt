package noncom.pino.locationlog.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.ActivityRecognitionResult.extractResult
import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import noncom.pino.locationlog.R
import noncom.pino.locationlog.database.LocationLogDB
import noncom.pino.locationlog.database.LocationLogEntry
import noncom.pino.locationlog.utils.ActivityListenerService.Actions
import java.sql.Timestamp
import kotlin.math.pow
import kotlin.math.sqrt


class LocationLoggerService: Service() {

    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var request: LocationRequest

    enum class Actions { STOP }

    override fun onBind(p0: Intent?): IBinder? { return null }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action) {

            noncom.pino.locationlog.utils.ActivityListenerService.Actions.STOP.toString() -> stopTrackingService()
        }

        if (ActivityRecognitionResult.hasResult(intent)) {

            val result = intent?.let { extractResult(it) }
            val probableActivities = result?.probableActivities
            val mostProbableActivity = probableActivities?.maxByOrNull { it.confidence }

            Log.d("LoggerService", "Detected activity: ${mostProbableActivity?.type}, confidence: ${mostProbableActivity?.confidence}")

            when (mostProbableActivity?.type) {

                DetectedActivity.STILL -> { stopTrackingService() }
                DetectedActivity.WALKING, DetectedActivity.RUNNING, DetectedActivity.ON_FOOT -> { startTrackingService(10) }
                DetectedActivity.ON_BICYCLE, DetectedActivity.IN_VEHICLE -> { startTrackingService(5) }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }


    @SuppressLint("MissingPermission")
    private fun startTrackingService(intervalInSecs: Long) {

        // TODO switch SDK to lowest possible
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel("logger_channel", "Logger Notification", NotificationManager.IMPORTANCE_MIN)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

        // build notification and start foreground service
        val notification = NotificationCompat.Builder(this, "logger_channel")
            .setOngoing(true)
            .setContentTitle("logging data")
            .setSmallIcon(R.drawable.ic_timeline_black_24dp)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .setShowWhen(false)
            .build()

        startForeground(1, notification)

        // ========== actual tracking ==========

        // instantiating components needed to fetch get location data
        locationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        locationCallback = object: LocationCallback() { override fun onLocationResult(result: LocationResult) { result.locations.forEach { saveData(it) } } }
        request = LocationRequest.Builder(intervalInSecs*1000L).build()

        locationClient.requestLocationUpdates(request, locationCallback, null)
    }

    private fun stopTrackingService() {

        locationClient.removeLocationUpdates(locationCallback)
        stopSelf()
    }

    // checks data point for quality and stores qualified data
    private fun saveData(location: Location) {

        val entry = LocationLogEntry(System.currentTimeMillis(), location.latitude, location.longitude)

        // only stores entry if location is at least delta far away
        CoroutineScope(Dispatchers.IO).launch {

            val delta = 0.0002 // = approx 20m
            var diff = 1.0

            val dao = LocationLogDB.getDatabase(applicationContext).dao()

            if (!dao.isEmpty()) {

                val lastLocation = dao.getLastLocation()
                val latDiff = lastLocation.latitude - entry.latitude
                val lngDiff = lastLocation.longitude - entry.longitude
                diff = sqrt(latDiff.pow(2) + lngDiff.pow(2)) // euclidean distance
            }

            if (diff >= delta) {

                try { dao.insertLocation(entry) } catch (e: Exception) { Log.e("Location", e.toString()) }
                Log.d("Location", "STORED: ${entry.latitude} : ${entry.longitude} at ${Timestamp(entry.timestamp)}")
            }
            else { Log.d("Location", "NOT STORED (diff=${diff}): ${entry.latitude} : ${entry.longitude} at ${Timestamp(entry.timestamp)}") }
        }
    }
}