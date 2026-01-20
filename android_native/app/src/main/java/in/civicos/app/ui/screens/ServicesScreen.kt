package in.civicos.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ServicesScreen(paddingValues: PaddingValues) {
    Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
        Text("Gov Services", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text("Legal Drafting", style = MaterialTheme.typography.titleMedium)
        ServiceCard("Draft RTI Application", "Get info from any dept")
        ServiceCard("Gas Connection Transfer", "Move Indane/HP gas")
        ServiceCard("Change Address", "Aadhaar/Voter ID")
        
        Spacer(modifier = Modifier.height(20.dp))
        
        Text("Welfare Schemes", style = MaterialTheme.typography.titleMedium)
        ServiceCard("Check Eligibility", "Find schemes for you")
        ServiceCard("Ayushman Bharat", "Health Insurance")
    }
}

@Composable
fun ServiceCard(title: String, subtitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).clickable { },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}
