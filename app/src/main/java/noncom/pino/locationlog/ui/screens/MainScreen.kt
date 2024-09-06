package noncom.pino.locationlog.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import noncom.pino.locationlog.ui.BottomNavGraph
import noncom.pino.locationlog.ui.StateProvider
import noncom.pino.locationlog.utils.AppState


@Preview
@Composable
fun MainScreen(@PreviewParameter(StateProvider::class) state: AppState) {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    )
    { innerPadding ->

        Box (
            modifier = Modifier.padding(PaddingValues(bottom = innerPadding.calculateBottomPadding()))
        ) {
            BottomNavGraph(state, navController = navController)
        }
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