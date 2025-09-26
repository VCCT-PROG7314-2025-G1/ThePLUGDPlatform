package com.example.plugd.ui.screens.plug

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.plugd.data.EventEntity
import com.example.plugd.ui.screens.nav.AddTopBar
import com.example.plugd.viewmodels.EventViewModel
import kotlinx.coroutines.launch

@Composable
fun AddPlugScreen(
    navController: NavHostController,
    eventViewModel: EventViewModel
) {
    val scope = rememberCoroutineScope()

    // Remember input state
    var pluggingWhat by remember { mutableStateOf("") }
    var plugCategory by remember { mutableStateOf("") }
    var plugTitle by remember { mutableStateOf("") }
    var plugDescription by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var supportDocs by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AddTopBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title
            Text(
                text = "Create a Plug",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            // Description
            Text(
                text = "Submit an application for services, jobs, gig opportunities & collaborative opportunities.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(18.dp))

            // Input Fields
            InputField(label = "What are you plugging?", value = pluggingWhat, onValueChange = { pluggingWhat = it }, placeholder = "ex. Looking for an artist for a gig on Friday.")
            InputField(label = "Plug Category", value = plugCategory, onValueChange = { plugCategory = it }, placeholder = "ex. Gig Opportunity")
            InputField(label = "Plug Title", value = plugTitle, onValueChange = { plugTitle = it }, placeholder = "ex. Artist wanted")
            InputField(label = "Plug Description", value = plugDescription, onValueChange = { plugDescription = it }, placeholder = "ex. Date: 10/07/2025 | Time: 6-8PM | Venue: Openwine")
            InputField(label = "Location", value = location, onValueChange = { location = it }, placeholder = "ex. 31 Bree Street Cape Town")
            InputField(label = "Upload Support Documents", value = supportDocs, onValueChange = { supportDocs = it }, placeholder = "Choose files...png, jpeg, pdf")

            Spacer(Modifier.height(16.dp))

            // Submit button
            Button(
                onClick = {
                    val newEvent = EventEntity(
                        id = System.currentTimeMillis(),
                        name = plugTitle,
                        location = location,
                        date = System.currentTimeMillis(),
                        category = plugCategory,
                        description = plugDescription,
                        createdBy = "CurrentUser" // you can replace with actual user
                    )
                    scope.launch {
                        eventViewModel.addEvent(newEvent)
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }
        }
    }
}

// --- Reusable Input Field ---
@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit, placeholder: String = "") {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}