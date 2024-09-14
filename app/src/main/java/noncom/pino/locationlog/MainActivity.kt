package noncom.pino.locationlog

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import noncom.pino.locationlog.database.LocationLogDB
import noncom.pino.locationlog.ui.screens.MainScreen
import noncom.pino.locationlog.utils.ActivityListenerService
import noncom.pino.locationlog.utils.AppState
import noncom.pino.locationlog.utils.Settings
import java.time.ZoneId


class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        lateinit var state: AppState

        ActivityListenerService()

        // TODO check all required permissions

        CoroutineScope(Dispatchers.IO).launch {

            // TODO: make timeline auto update (Flow/ViewModel)
            // TODO: check whether lists are lazy
            val db = LocationLogDB.getDatabase(applicationContext).dao().getLocationsNewestFirst()
            val settings = Settings(ZoneId.of("UTC")) // not used
            state = AppState(db, settings, applicationContext)
        }

        enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT))
        setContent { Box { MainScreen(state) } }
    }
//    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//        // ask for permission if not already granted
//        ActivityCompat.requestPermissions(
//            this@MainActivity, arrayOf(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ), 1002
//        )
//    }
}