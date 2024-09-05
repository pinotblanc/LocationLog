package noncom.pino.locationlog.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import noncom.pino.locationlog.ui.screens.BottomBarScreen
import noncom.pino.locationlog.ui.screens.MapScreen
import noncom.pino.locationlog.ui.screens.SettingsScreen
import noncom.pino.locationlog.ui.screens.TimelineScreen
import noncom.pino.locationlog.utils.AppState

@Composable
fun BottomNavGraph(state: AppState, navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Map.route
    ) {
        composable(route = BottomBarScreen.Map.route) { MapScreen(state) }
        composable(route = BottomBarScreen.Timeline.route) { TimelineScreen(state) }
        composable(route = BottomBarScreen.Settings.route) { SettingsScreen(state) }
    }
}