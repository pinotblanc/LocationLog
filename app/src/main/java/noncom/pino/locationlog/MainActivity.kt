package noncom.pino.locationlog

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import noncom.pino.locationlog.database.LocationLogDB
import java.util.concurrent.TimeUnit


class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // TODO: make timeline auto update (LiveData)
        CoroutineScope(Dispatchers.IO).launch {

            val db = LocationLogDB.getDatabase(applicationContext).dao().getLocationsNewestFirst()
            try { setContent { LocationLogApp(db) } }
            catch (e: Exception) { Log.d("Location", e.toString()) }
        }
    }

    override fun onResume() {

        super.onResume()
        startTracking()
    }

    private fun startTracking() {

        // first, check for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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