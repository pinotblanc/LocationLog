package noncom.pino.locationlog.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Map: BottomBarScreen(
        route = "map",
        title = "Map",
        icon = Icons.Default.LocationOn
    )
    data object Timeline: BottomBarScreen(
        route = "timeline",
        title = "Timeline",
        icon = Icons.Default.Menu
    )
    data object Settings: BottomBarScreen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}