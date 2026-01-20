package in.civicos.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import in.civicos.app.ui.theme.CivicOSTheme
import in.civicos.app.ui.screens.PanicScreen
import in.civicos.app.ui.screens.GrievanceScreen
import in.civicos.app.ui.screens.ServicesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CivicOSTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CivicOSApp()
                }
            }
        }
    }
}

@Composable
fun CivicOSApp() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Text("🆘") },
                    label = { Text("Emergency") },
                    selected = true,
                    onClick = { navController.navigate("panic") }
                )
                NavigationBarItem(
                    icon = { Text("📸") },
                    label = { Text("Grievance") },
                    selected = false,
                    onClick = { navController.navigate("grievance") }
                )
                NavigationBarItem(
                    icon = { Text("📜") },
                    label = { Text("Services") },
                    selected = false,
                    onClick = { navController.navigate("services") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "panic") {
            composable("panic") { PanicScreen(innerPadding) }
            composable("grievance") { GrievanceScreen(innerPadding) }
            composable("services") { ServicesScreen(innerPadding) }
        }
    }
}
