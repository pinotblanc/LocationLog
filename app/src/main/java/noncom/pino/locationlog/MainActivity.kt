package noncom.pino.locationlog

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import noncom.pino.locationlog.database.LocationLogDB
import noncom.pino.locationlog.ui.screens.MainScreen
import noncom.pino.locationlog.utils.AppState
import noncom.pino.locationlog.utils.LocatingWorker
import noncom.pino.locationlog.utils.Settings
import java.time.ZoneId
import java.util.concurrent.TimeUnit


class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        lateinit var state: AppState

        CoroutineScope(Dispatchers.IO).launch {

            // TODO: make timeline auto update (Flow/ViewModel)
            val db = LocationLogDB.getDatabase(applicationContext).dao().getLocationsNewestFirst()
            val settings = Settings(ZoneId.of("UTC")) // not used
            state = AppState(db, settings)
        }

        enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT))
        setContent { Box { MainScreen(state) } }
    }

    override fun onResume() {

        super.onResume()
        startTracking()
    }

    private fun startTracking() {

        // check for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // ask for permission if not already granted
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 1002
            )
        }

        // start periodic location tracking (15min interval)
        val periodicWork = PeriodicWorkRequest.Builder(LocatingWorker::class.java, 15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this@MainActivity).enqueueUniquePeriodicWork("Location", ExistingPeriodicWorkPolicy.UPDATE, periodicWork)
    }
}