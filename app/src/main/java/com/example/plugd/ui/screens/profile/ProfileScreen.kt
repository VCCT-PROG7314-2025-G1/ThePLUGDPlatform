package com.example.plugd.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.plugd.ui.screens.nav.ProfileTopBar
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    LaunchedEffect(userId) {
        viewModel.loadUser(userId)
    }

    val user by viewModel.user.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = { ProfileTopBar(navController) }
    ) { padding ->
        when {
            loading -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            user != null -> {
                val u = user!!
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Profile picture + username
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(80.dp).clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "Profile", Modifier.size(50.dp))
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(u.username, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(u.role ?: "User", fontSize = 14.sp, color = Color.DarkGray)
                            Text("• ${u.location ?: "Unknown"}", fontSize = 12.sp, color = Color.Gray)
                        }
                    }

                    // Bio
                    Card(
                        Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.05f))
                    ) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Bio", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(Modifier.height(4.dp))
                            Text(u.bio ?: "No bio available", fontSize = 13.sp, color = Color.DarkGray)
                        }
                    }

                    // Followers count
                    Text("Followers: ${u.followersCount ?: 0}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }

            else -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(error ?: "User not found")
            }
        }
    }
}

