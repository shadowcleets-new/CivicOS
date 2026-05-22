package com.nivar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.nivar.app.data.model.User as NivarUser
import com.nivar.app.ui.screens.*
import com.nivar.app.ui.theme.NivarTheme
import com.nivar.app.ui.theme.ErrorRed
import com.nivar.app.ui.viewmodel.AuthViewModel
import com.nivar.app.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

// ===== Navigation Destination Model =====
data class NavDestination(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // [SECURITY] Screen Privacy - Prevent screenshots/recording of sensitive data
        com.nivar.app.utils.SecurityUtils.setScreenPrivacy(this, true)

        // [SECURITY] Environment Integrity Check
        if (com.nivar.app.utils.SecurityUtils.isDeviceRooted()) {
             android.util.Log.e("SECURITY", "ROOT ACCESS DETECTED - APP INTEGRITY COMPROMISED")
        }

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val authViewModel: AuthViewModel = hiltViewModel()
            
            val themePreference by settingsViewModel.themePreference.collectAsState()
            val currentUser by authViewModel.currentUser.collectAsState()
            
            val darkTheme = when (themePreference) {
                "light" -> false
                "dark" -> true
                else -> androidx.compose.foundation.isSystemInDarkTheme()
            }

            // Initialize Constitution Data
            val context = androidx.compose.ui.platform.LocalContext.current
            LaunchedEffect(Unit) {
                com.nivar.app.data.model.ConstitutionRepository.loadArticles(context)
            }
            
            NivarTheme(darkTheme = darkTheme) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    if (currentUser == null) {
                        LoginScreen(
                            onSignInAnonymously = { authViewModel.signInAnonymously() },
                            onSignInWithGoogle = { /* Google Sign In Launcher */ },
                            onSignInWithEmail = { e, p -> /* Email Sign In */ }
                        )
                    } else {
                        NivarApp(
                            windowSizeClass = windowSizeClass.widthSizeClass, 
                            user = currentUser!!, 
                            authViewModel = authViewModel, 
                            settingsViewModel = settingsViewModel
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NivarApp(
    windowSizeClass: WindowWidthSizeClass,
    user: NivarUser,
    authViewModel: AuthViewModel,
    settingsViewModel: SettingsViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val destinations = listOf(
        NavDestination("services", "Services", Icons.AutoMirrored.Filled.List, Icons.AutoMirrored.Outlined.List),
        NavDestination("directory", "Directory", Icons.Filled.Search, Icons.Outlined.Search),
        NavDestination("constitution", "Rights", Icons.Filled.AccountBalance, Icons.Outlined.AccountBalance),
        NavDestination("panic", "SOS", Icons.Filled.Warning, Icons.Outlined.Warning),
        NavDestination("more", "More", Icons.Filled.Person, Icons.Outlined.Person)
    )

    val showBottomBar = currentDestination?.route in destinations.map { it.route }
    
    Scaffold(
        bottomBar = {
            if (showBottomBar && windowSizeClass == WindowWidthSizeClass.Compact) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                    tonalElevation = 8.dp, 
                    modifier = Modifier.height(84.dp)
                ) {
                    destinations.forEach { dest ->
                        val selected = currentDestination?.route == dest.route
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    if (selected) dest.selectedIcon else dest.unselectedIcon,
                                    contentDescription = dest.label,
                                    modifier = Modifier.size(if (selected) 28.dp else 24.dp),
                                    tint = if (dest.route == "panic") ErrorRed
                                           else if (selected) MaterialTheme.colorScheme.primary
                                           else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            label = {
                                Text(
                                    dest.label,
                                    style = if (selected) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelSmall,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (dest.route == "panic") ErrorRed
                                            else if (selected) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            selected = selected,
                            onClick = {
                                if (currentDestination?.route != dest.route) {
                                    navController.navigate(dest.route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = if (dest.route == "panic")
                                    ErrorRed.copy(alpha = 0.15f)
                                else
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Row(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            if (windowSizeClass != WindowWidthSizeClass.Compact) {
                NavigationRail(containerColor = MaterialTheme.colorScheme.surface) {
                    destinations.forEach { dest ->
                        val selected = currentDestination?.route == dest.route
                        NavigationRailItem(
                            icon = {
                                Icon(
                                    if (selected) dest.selectedIcon else dest.unselectedIcon,
                                    contentDescription = dest.label,
                                    tint = if (dest.route == "panic") ErrorRed
                                           else if (selected) MaterialTheme.colorScheme.primary
                                           else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            label = { Text(dest.label, style = MaterialTheme.typography.labelSmall) },
                            selected = selected,
                            onClick = {
                                navController.navigate(dest.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }

            NavHost(
                navController = navController,
                startDestination = "services",
                modifier = Modifier.weight(1f),
                enterTransition = {
                    fadeIn(animationSpec = tween(500)) +
                    slideInHorizontally(animationSpec = tween(500), initialOffsetX = { it / 6 })
                },
                exitTransition = { fadeOut(animationSpec = tween(400)) },
                popEnterTransition = { fadeIn(animationSpec = tween(500)) },
                popExitTransition = {
                    fadeOut(animationSpec = tween(400)) +
                    slideOutHorizontally(animationSpec = tween(500), targetOffsetX = { it / 6 })
                }
            ) {
                composable("services") { ServicesScreen(navController = navController) }
                composable("directory") {
                    DirectoryScreen(
                        onMinistryClick = { ministryId ->
                            navController.navigate("ministry_detail/$ministryId")
                        }
                    )
                }
                composable(
                    "ministry_detail/{ministryId}",
                    arguments = listOf(navArgument("ministryId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val ministryId = backStackEntry.arguments?.getString("ministryId") ?: ""
                    MinistryDetailScreen(
                        ministryId = ministryId,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable("constitution") { ConstitutionScreen() }
                composable("panic") { PanicScreen() }
                composable("more") {
                    MoreScreen(
                        navController = navController,
                        user = user,
                        onSignOut = { authViewModel.signOut() }
                    )
                }
                composable("profile") {
                    ProfileScreen(
                        user = user,
                        onSaveProfile = { authViewModel.updateProfile(it) },
                        onSignOut = { authViewModel.signOut() }
                    )
                }
                composable("settings") {
                    val themePreference by settingsViewModel.themePreference.collectAsState()
                    SettingsScreen(
                        themePreference = themePreference,
                        onThemeChange = { settingsViewModel.setThemePreference(it) }
                    )
                }
                composable("grievance") { GrievanceScreen() }
            }
        }
    }
}

@Composable
fun MoreScreen(
    navController: androidx.navigation.NavController,
    user: NivarUser,
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Account", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(32.dp))

        MoreItem("Profile", Icons.Default.Person) { navController.navigate("profile") }
        MoreItem("Settings", Icons.Default.Settings) { navController.navigate("settings") }
        MoreItem("Help & Feedback", Icons.Default.Help) { /* TODO */ }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = onSignOut,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Sign Out")
        }
    }
}

@Composable
fun MoreItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}
