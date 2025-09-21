package com.example.plugd.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plugd.ui.navigation.Routes
import com.example.plugd.ui.utils.PreviewNavController

@Composable
fun RoleSelectionScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Select Your Role")
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { navController.navigate(Routes.LOGIN) }) {
            Text("Artist")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { navController.navigate(Routes.LOGIN) }) {
            Text("Event/Venue")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { navController.navigate(Routes.LOGIN) }) {
            Text("Fan")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRoleSelectionScreen() {
    RoleSelectionScreen(navController = rememberNavControllerPreview())
}