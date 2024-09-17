package noncom.pino.locationlog.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import noncom.pino.locationlog.R
import noncom.pino.locationlog.database.LocationLogDB
import noncom.pino.locationlog.database.LocationLogEntry
import java.sql.Timestamp
import kotlin.math.pow
import kotlin.math.sqrt

class LocationLoggerService: Service() {

    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private var isTracking = false
    private var repeatDataCounter = 0
    private val serviceScope = CoroutineScope(Dispatchers.Default + Job())

    override fun onCreate() { super.onCreate()

        locationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.locations.forEach { saveData(it) }
            }
        }
    }

    override fun onDestroy() { super.onDestroy()

        if (isTracking) stopTrackingService()
        serviceScope.cancel()
    }

    enum class Actions { STOP }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        // case: listener sends stop -> user manually stopped tracking
        if (intent?.action == Actions.STOP.toString()) stopTrackingService()

        // starts tracking with interval matching to recognized activity type
        if (ActivityRecognitionResult.hasResult(intent)) {

            val result = intent?.let { ActivityRecognitionResult.extractResult(it) }
            val mostProbableActivity = result?.probableActivities?.maxByOrNull { it.confidence }

            Log.d("LocationLog", "Detected activity: ${mostProbableActivity?.type}, confidence: ${mostProbableActivity?.confidence}")

            when (mostProbableActivity?.type) {

                DetectedActivity.WALKING, DetectedActivity.RUNNING, DetectedActivity.ON_FOOT -> startTrackingService(10)
                DetectedActivity.ON_BICYCLE, DetectedActivity.IN_VEHICLE -> startTrackingService(5)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    @SuppressLint("MissingPermission")
    private fun startTrackingService(intervalInSecs: Long) {

        if (isTracking) {

            Log.d("LocationLog", "LocationLogger already in progress")
            return
        }

        // notification channel only required with sdk 29+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel("logger_channel", "Logger Notification", NotificationManager.IMPORTANCE_MIN)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // initialize FGS notification and start FGS
        val notification = NotificationCompat.Builder(this, "logger_channel")
            .setOngoing(true)
            .setContentTitle("logging data")
            .setSmallIcon(R.drawable.ic_timeline_black_24dp)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .setShowWhen(false)
            .build()

        // start foreground service considering android version
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) startForeground(1, notification)
        else startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)

        // makes sure the counter always starts at 0
        repeatDataCounter = 0

        // TODO check if Priority.PRIORITY_BALANCED_POWER_ACCURACY(=default) is enough
        // initialize request and start tracking
        val request = LocationRequest.Builder(intervalInSecs*1000L).setPriority(Priority.PRIORITY_HIGH_ACCURACY).build()

        try {
            locationClient.requestLocationUpdates(request, locationCallback, null)
            isTracking = true
            Log.d("LocationLog", "Started tracking with interval: $intervalInSecs seconds")
        }
        catch (e: SecurityException) {
            Log.e("LocationLog", "Error starting location updates", e)
        }
    }

    private fun stopTrackingService() {

        if (!isTracking) {
            Log.d("LocationLog", "LocationLogger is not active")
            return
        }
        isTracking = false

        locationClient.removeLocationUpdates(locationCallback)
        stopSelf()
    }

    private fun saveData(location: Location) {
        val entry = LocationLogEntry(System.currentTimeMillis(), location.latitude, location.longitude)

        serviceScope.launch {

            val deltaMin = 0.0002   // = approx 20m
            val deltaMax = 0.005    // = approx 500m
            var diff = 0.0004       // default value (-> when db empty) in range

            val dao = LocationLogDB.getDatabase(applicationContext).dao()

            if (!dao.isEmpty()) {

                val lastLocation = dao.getLastLocation()
                val latDiff = lastLocation.latitude - entry.latitude
                val lngDiff = lastLocation.longitude - entry.longitude
                diff = sqrt(latDiff.pow(2) + lngDiff.pow(2)) // euclidean distance
            }

            // handles data appropriately to distance
            if (diff in deltaMin..deltaMax) {

                try {
                    dao.insertLocation(entry)
                    Log.d("LocationLog", "STORED: ${entry.latitude} : ${entry.longitude} at ${Timestamp(entry.timestamp)}")
                }
                catch (e: Exception) { Log.e("LocationLog", "Error inserting location", e) }
            }
            else {

                // checks if user fully stopped moving
                if(++repeatDataCounter > 5) {

                    // start ActivityListener
                    Intent(applicationContext, ActivityListenerService::class.java).also {

                        it.action = ActivityListenerService.Actions.START.toString()
                        applicationContext.startService(it)
                    }
                    stopTrackingService()
                }
                Log.d("Location", "NOT STORED (diff=${diff}): ${entry.latitude} : ${entry.longitude} at ${Timestamp(entry.timestamp)}")
            }
        }
    }
}