package com.example.plugd.ui.screens.nav

import GoogleAuthUiClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.plugd.ui.navigation.Routes
import com.example.plugd.ui.screens.auth.LoginScreen
import com.example.plugd.ui.screens.auth.RegisterScreen
import com.example.plugd.ui.screens.home.HomeScreen
import com.example.plugd.remote.EventRemoteDataSource
import com.example.plugd.remote.RetrofitInstance
import com.example.plugd.repository.EventRepository
import com.example.plugd.ui.screens.activity.ActivityScreen
import com.example.plugd.ui.screens.home.CommunityScreen
import com.example.plugd.ui.screens.home.FilterScreen
import com.example.plugd.ui.screens.plug.AddPlugScreen
import com.example.plugd.ui.screens.profile.ProfileScreen
import com.example.plugd.ui.screens.profile.SettingsScreen
import com.example.plugd.viewmodelfactory.EventViewModelFactory
import com.example.plugd.viewmodels.EventViewModel

@Composable
fun AppNavHost(startDestination: String) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    // Event ViewModel setup
    val apiService = RetrofitInstance.api
    val remoteDataSource = EventRemoteDataSource(apiService)
    val eventRepository = EventRepository(remoteDataSource)
    val eventViewModel: EventViewModel = viewModel(
        factory = EventViewModelFactory(eventRepository)
    )

    val googleAuthClient = remember {
        GoogleAuthUiClient(context = context)
    }

    NavHost(navController, startDestination = Routes.REGISTER) {

        // Register page
        composable(Routes.REGISTER) {
            RegisterScreen(navController = navController)
        }

        // Login page
        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Home page
        composable(Routes.HOME) {
            MainScreenWithBottomNav(
                navController = navController,
                topBar = { HomeTopBar(navController = navController) } // use your exact HomeTopBar
            ) { padding ->
                HomeScreen(
                    navController = navController,
                )
            }
        }

        // Community Screen
        composable(Routes.COMMUNITY) {
            MainScreenWithBottomNav(
                navController = navController,
                topBar = { CommunityTopBar(navController = navController) } // use your exact HomeTopBar
            ) { padding ->
                CommunityScreen(
                    navController = navController,
                )
            }
        }

        // Add Plug Screen
        composable(Routes.ADD) {
            AddPlugScreen(
                navController = navController,
                eventViewModel = eventViewModel
            )
        }

        // Activity screen
        composable(Routes.ACTIVITY) {
            MainScreenWithBottomNav(
                navController = navController,
                topBar = { ActivityTopBar(navController = navController) } // use your exact HomeTopBar
            ) { padding ->
                ActivityScreen(
                    navController = navController,
                )
            }
        }

        // Profile Screen
        composable(Routes.PROFILE) {
            MainScreenWithBottomNav(
                navController = navController,
                topBar = { ProfileTopBar(navController = navController) } // use your exact HomeTopBar
            ) { padding ->
                ProfileScreen(
                    navController = navController
                )
            }
        }

        // Settings Page
        composable(Routes.SETTINGS) { SettingsScreen(navController = navController) }

        // Filter Page
        composable(Routes.FILTER) { FilterScreen(navController = navController) }
    }
}