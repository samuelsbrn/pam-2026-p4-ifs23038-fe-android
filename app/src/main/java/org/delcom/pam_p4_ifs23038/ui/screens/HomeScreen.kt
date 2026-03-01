package org.delcom.pam_p4_ifs23038.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.delcom.pam_p4_ifs23038.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23038.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23038.ui.theme.DelcomTheme

@Composable
fun HomeScreen(
    navController: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBarComponent(navController = navController, title = "Home", false)
        Box(modifier = Modifier.weight(1f)) {
            HomeUI()
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun HomeUI(){
    Column(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Text(
                style = MaterialTheme.typography.headlineMedium,
                text = "🌿 Delcom Plants & Motors 🏍️",
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            // Ikon Tanaman
            Card(modifier = Modifier.padding(8.dp)) {
                Text(text = "🌿", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineLarge)
            }
            // Ikon Motor
            Card(modifier = Modifier.padding(8.dp)) {
                Text(text = "🏍️", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineLarge)
            }
        }
    }
}
