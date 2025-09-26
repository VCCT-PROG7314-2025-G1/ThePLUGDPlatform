package com.example.plugd.ui.screens.activity

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plugd.ui.screens.nav.ActivityTopBar

@Composable
fun ActivityScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            ActivityTopBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Activity",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "This is a placeholder for the activity page.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}