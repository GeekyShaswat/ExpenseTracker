package com.example.navigation

import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.screens.AddData
import com.example.screens.HomeScreen
import com.example.screens.Login
import com.example.screens.MainLayer
import com.example.screens.SignUp
import com.example.expensetracker.R
import com.example.screens.NotificationScreen
import com.example.screens.Stats
import com.example.viewModel.NameViewModel

@Composable
fun NavHostPage() {
    // Use a single navController instance
    val navController = rememberNavController()
    var bottomBarVisibility by remember { mutableStateOf(true) }
    var nameViewModel : NameViewModel = viewModel()

    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = bottomBarVisibility) {
                NavigationBottomBar(
                    navController = navController,
                    items = listOf(
                        NavItem(R.drawable.home_icon, "home"),
                        NavItem(R.drawable.stat_icon, "stats")
                    )
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route="notification"){
                bottomBarVisibility = false
                NotificationScreen(navController)
            }
            composable(route = "login") {
                bottomBarVisibility = false
                Login(navController,nameViewModel)
            }
            composable(route = "signup") {
                bottomBarVisibility = false
                SignUp(navController)
            }
            composable(route = "home") {
                bottomBarVisibility = true
                HomeScreen(navController,nameViewModel)
            }
            composable(route = "add") {
                bottomBarVisibility = false
                AddData(navController)
            }
            composable(route = "profile") {
                bottomBarVisibility = false
                MainLayer(navController)
            }
            composable(route = "stats") {
                bottomBarVisibility = true
                Stats(navController)
            }
        }
    }
}

data class NavItem(
    val icon: Int,
    val route: String
)
@Composable
fun NavigationBottomBar(
    navController: NavController,
    items: List<NavItem>
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    // The current route is compared directly with the item's route.
    val currentRoute = navBackStackEntry.value?.destination?.route
    BottomAppBar(modifier = Modifier.height(60.dp)) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {

                        navController.navigate(item.route) {
                            popUpTo("home"){
                                inclusive =false
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true

                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = null
                    )
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(R.color.bggreen),
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = colorResource(R.color.bggreen),
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NavHostPreview() {
    NavHostPage()
}
