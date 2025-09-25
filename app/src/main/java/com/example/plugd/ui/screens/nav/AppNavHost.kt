package com.example.plugd.ui.screens.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.plugd.data.hasSeenOnboarding
import com.example.plugd.data.saveOnboardingCompleted
import com.example.plugd.remote.ApiService
import com.example.plugd.ui.navigation.Routes
import com.example.plugd.ui.screens.auth.LoginScreen
import com.example.plugd.ui.screens.auth.RegisterScreen
import com.example.plugd.ui.screens.auth.RoleSelectionScreen
import com.example.plugd.ui.screens.home.HomeScreen
import com.example.plugd.ui.screens.onboarding.OnboardingScreen
import com.example.plugd.remote.EventRemoteDataSource
import com.example.plugd.remote.RetrofitInstance
import com.example.plugd.repository.EventRepository
import com.example.plugd.ui.auth.GoogleAuthUiClient
import com.example.plugd.ui.screens.plug.AddPlugScreen
import com.example.plugd.ui.screens.profile.ProfileScreen
import com.example.plugd.ui.screens.settings.SettingsScreen
import com.example.plugd.viewmodelfactory.EventViewModelFactory
import com.example.plugd.viewmodels.EventViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun AppNavHost() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    // 🔹 Onboarding preference
    val seenOnboarding by context.hasSeenOnboarding().collectAsState(initial = false)
    val startDestination = if (!seenOnboarding) Routes.ONBOARDING else Routes.REGISTER

    // 🔹 Event ViewModel setup
    val apiService = RetrofitInstance.api
    val remoteDataSource = EventRemoteDataSource(apiService)
    val eventRepository = EventRepository(remoteDataSource)
    val eventViewModel: EventViewModel = viewModel(
        factory = EventViewModelFactory(eventRepository)
    )

    // 🔹 Google One Tap client setup
    val oneTapClient = Identity.getSignInClient(context)
    val googleAuthClient = remember {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = oneTapClient
        )
    }

    NavHost(navController, startDestination = startDestination) {

        // Onboarding
        composable(Routes.ONBOARDING) {
            OnboardingScreen(navController) {
                scope.launch {
                    context.saveOnboardingCompleted() // default is Main
                }
                navController.navigate(Routes.ROLE) {
                    popUpTo(Routes.ONBOARDING) { inclusive = true }
                }
            }
        }

        // Role selection
        composable(Routes.ROLE) {
            RoleSelectionScreen(navController) { selectedRole ->
                navController.navigate("${Routes.REGISTER}?role=$selectedRole") {
                    popUpTo(Routes.ROLE) { inclusive = true }
                }
            }
        }

        // Register
        composable(
            "${Routes.REGISTER}?role={role}",
            arguments = listOf(navArgument("role") {
                type = NavType.StringType
                defaultValue = "Artist" // fallback if not passed
            })
        ) { backStackEntry ->
            // Extract role safely
            val role = backStackEntry.arguments?.getString("role") ?: "Artist"

            // Pass it to your screen
            RegisterScreen(navController, role, googleAuthClient)
        }
        // Login
        composable(Routes.LOGIN) {
            LoginScreen(
                navController = navController,
                googleAuthClient = googleAuthClient
            )
        }

        // Home screen with role parameter
        composable("${Routes.HOME}?role={role}",
            arguments = listOf(navArgument("role") {
                type = NavType.StringType
                defaultValue = "Unknown"
            })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getString("role") ?: "Unknown"

            MainScreenWithBottomNav(navController) { padding ->
                HomeScreen(
                    navController = navController,
                    role = role,
                    eventViewModel = eventViewModel
                )
            }
        }

        composable(Routes.ADD) {
            AddPlugScreen(
                navController = navController,
                eventViewModel = eventViewModel // keep this in case you want to use it later
            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(navController = navController)
        }

        composable("settings") {
            SettingsScreen(navController = navController)
        }
    }
}