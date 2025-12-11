package com.appsim.tencent_meeting_sim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.appsim.tencent_meeting_sim.ui.theme.TencentMeetingTheme
import com.appsim.tencent_meeting_sim.data.repository.DataRepository
import com.appsim.tencent_meeting_sim.navigation.TencentMeetingNavGraph
import com.appsim.tencent_meeting_sim.navigation.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize data files: copy data from assets to filesDir
        val dataRepository = DataRepository.getInstance(this)
        dataRepository.initializeDataFiles()

        setContent {
            TencentMeetingTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()

    // Track current destination for bottom bar visibility and status bar color
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Bottom navigation items
    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home.route, "会议", Icons.Default.VideoCall),
        BottomNavItem(Screen.Contact.route, "通讯录", Icons.Default.Contacts),
        BottomNavItem(Screen.Me.route, "我的", Icons.Default.Person)
    )

    // Determine if bottom bar should be shown
    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    // Configure status bar color based on current screen
    val statusBarColor = when {
        currentRoute?.startsWith("meeting_details") == true -> Color(0xFF1F2227)
        currentRoute?.startsWith("meeting_chat") == true -> Color(0xFF1F2227)
        currentRoute?.startsWith("meeting_replay") == true -> Color(0xFF1F2227)
        else -> Color(0xFFE3F2FD)
    }

    val darkIcons = when {
        currentRoute?.startsWith("meeting_details") == true -> false
        currentRoute?.startsWith("meeting_chat") == true -> false
        currentRoute?.startsWith("meeting_replay") == true -> false
        else -> true
    }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = darkIcons
        )
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    items = bottomNavItems
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TencentMeetingNavGraph(
                navController = navController,
                modifier = Modifier.fillMaxSize(),
                onTabSelected = { tabIndex ->
                    // Handle tab switching from HomeScreen
                    val targetRoute = bottomNavItems.getOrNull(tabIndex)?.route ?: return@TencentMeetingNavGraph
                    navController.navigate(targetRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun BottomNavigationBar(
    navController: androidx.navigation.NavHostController,
    items: List<BottomNavItem>
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Gray
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 12.sp
                    )
                },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination and save state
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF1976D2),
                    selectedTextColor = Color(0xFF1976D2),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
