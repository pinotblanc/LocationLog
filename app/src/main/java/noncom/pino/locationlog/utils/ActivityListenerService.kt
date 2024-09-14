package noncom.pino.locationlog.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.* // TODO change
import noncom.pino.locationlog.R


class ActivityListenerService: Service() {

    private var interval = 5*1000L // TODO change
    private lateinit var pendingIntent: PendingIntent
    private lateinit var activityClient: ActivityRecognitionClient

    override fun onCreate() {
        super.onCreate()

        val intent = Intent(applicationContext, LocationLoggerService::class.java)
        pendingIntent = PendingIntent.getService(applicationContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)
        activityClient = ActivityRecognition.getClient(applicationContext)
    }

    enum class Actions { START, STOP }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when(intent?.action) {

            Actions.START.toString() -> startListeningService()
            Actions.STOP.toString() -> stopListeningService()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? { return null }

    @SuppressLint("MissingPermission")
    fun startListeningService() {

        // TODO switch SDK to lowest possible
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel("logger_channel", "Logger Notification", NotificationManager.IMPORTANCE_MIN)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

        // build notification and start foreground service
        val notification = NotificationCompat.Builder(this, "logger_channel")
            .setOngoing(true)
            .setContentTitle("service is active")
            .setSmallIcon(R.drawable.ic_map_black_24dp)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_SECRET)
            .setShowWhen(false)
            .build()

        startForeground(1, notification)

        activityClient.requestActivityUpdates(interval, pendingIntent)
    }

    @SuppressLint("MissingPermission")
    fun stopListeningService() {

        Intent(applicationContext, LocationLoggerService::class.java).also {

            it.action = LocationLoggerService.Actions.STOP.toString()
            applicationContext.startService(it)
        }
        activityClient.removeActivityUpdates(pendingIntent)
        stopSelf()
    }
}