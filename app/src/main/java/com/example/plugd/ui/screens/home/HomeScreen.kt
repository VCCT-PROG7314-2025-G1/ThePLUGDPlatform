package com.example.plugd.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plugd.data.EventEntity
import com.example.plugd.ui.screens.nav.HomeTopBar
import com.example.plugd.ui.screens.nav.ProfileTopBar
import com.example.plugd.ui.theme.Telegraf
import com.example.plugd.viewmodels.EventViewModel
import java.text.SimpleDateFormat
import java.util.*

// --- HomeScreen Composable ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
    fun HomeScreen(navController: NavHostController) {
        Scaffold(
            topBar = {
                HomeTopBar(navController = navController)
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
                    text = "PLUG IN TO THE LATEST",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontFamily = Telegraf
                    )
                )

                Text(
                    text = "This is a placeholder for the home page.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
