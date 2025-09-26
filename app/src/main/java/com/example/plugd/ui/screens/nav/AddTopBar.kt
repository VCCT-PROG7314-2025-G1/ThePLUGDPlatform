package com.example.plugd.ui.screens.nav

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import com.example.plugd.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTopBar(navController: NavHostController) {
    TopAppBar(
        title = { /* No title */ },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate("home_screen") {
                    popUpTo("home_screen") { inclusive = true }
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.btn_back),
                    contentDescription = "Back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.Black
        )
    )
}