package noncom.pino.locationlog.ui.screens

import android.annotation.SuppressLint
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import noncom.pino.locationlog.ui.BottomNavGraph
import noncom.pino.locationlog.utils.AppState


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(state: AppState) {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        BottomNavGraph(state, navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {

    val items = listOf(
        BottomBarScreen.Map,
        BottomBarScreen.Timeline,
        BottomBarScreen.Settings
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->

            NavigationBarItem(
                label = { Text(text = item.title) },
                icon =  { Icon(item.icon, contentDescription = item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = { navController.navigate(item.route) }
            )
        }
    }
}