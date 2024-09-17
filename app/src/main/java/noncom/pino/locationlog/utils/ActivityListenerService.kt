package noncom.pino.locationlog.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.* // TODO change
import noncom.pino.locationlog.R


class ActivityListenerService: Service() {

    private lateinit var pendingIntent: PendingIntent
    private lateinit var activityClient: ActivityRecognitionClient

    private var interval = 5*1000L // TODO change
    private var isListening = false

    override fun onCreate() { super.onCreate()

        val intent = Intent(applicationContext, LocationLoggerService::class.java)

        var flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) flags = PendingIntent.FLAG_UPDATE_CURRENT
        pendingIntent = PendingIntent.getService(applicationContext, 1, intent, flags)

        activityClient = ActivityRecognition.getClient(applicationContext)
    }

    // should never be called, only stopListeningService()
    @SuppressLint("MissingPermission")
    override fun onDestroy() { super.onDestroy()

        stopListeningService(false)
    }

    enum class Actions { START, STOP }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action) {

            Actions.START.toString() -> startListeningService()
            Actions.STOP.toString() -> stopListeningService(true)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? { return null }

    @SuppressLint("MissingPermission")
    fun startListeningService() {

        if (isListening) {

            Log.d("LocationLog", "ActigityListener already in progress")
            return
        }

        // TODO switch SDK to lowest possible
        // notification channel only required with sdk 29+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel("logger_channel", "Logger Notification", NotificationManager.IMPORTANCE_MIN)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

        // initialize FGS notification and start FGS
        val notification = NotificationCompat.Builder(this, "logger_channel")
            .setOngoing(true)
            .setSilent(true)
            .setContentTitle("service is active")
            .setSmallIcon(R.drawable.location_pin_24 )
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            //.setWhen(System.currentTimeMillis())
            //.setUsesChronometer(true)
            .setShowWhen(false)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()

        // start foreground service considering android version
        if (Build.VERSION.SDK_INT < 34) startForeground(1, notification)
        else startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH)

        try {
            activityClient.requestActivityUpdates(interval, pendingIntent)
            isListening = true
        }
        catch (e:Exception) { Log.e("LocationLog", "Failed to request activity updates", e)}
    }

    @SuppressLint("MissingPermission")
    fun stopListeningService(stopLogger: Boolean) {

        if (!isListening) {

            Log.d("LocationLog", "ActivityListener is not active")
            return
        }

        if (stopLogger) {

            Intent(applicationContext, LocationLoggerService::class.java).also {

                it.action = LocationLoggerService.Actions.STOP.toString()
                applicationContext.startService(it)
            }
        }
        activityClient.removeActivityUpdates(pendingIntent)
        stopSelf()
    }
}