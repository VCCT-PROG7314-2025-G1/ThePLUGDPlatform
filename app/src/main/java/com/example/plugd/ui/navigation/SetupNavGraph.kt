package com.example.plugd.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.plugd.ui.screens.home.HomeScreen
import com.example.plugd.ui.screens.auth.LoginScreen
import com.example.plugd.ui.screens.auth.RegisterScreen
import com.example.plugd.ui.screens.auth.RoleSelectionScreen

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.ROLE) {
        composable(Routes.ROLE) {
            RoleSelectionScreen(navController)
        }
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }
        composable(Routes.REGISTER) {
            RegisterScreen(navController)
        }
        composable(Routes.HOME) {
            HomeScreen(navController = navController, role = "Unknown")
        }
    }
}