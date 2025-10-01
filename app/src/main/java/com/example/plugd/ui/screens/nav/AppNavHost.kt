package com.example.plugd.ui.screens.nav

import GoogleAuthUiClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.plugd.ui.navigation.Routes
import com.example.plugd.ui.screens.auth.LoginScreen
import com.example.plugd.ui.screens.auth.RegisterScreen
import com.example.plugd.ui.screens.home.HomeScreen
import com.example.plugd.ui.screens.home.CommunityScreen
import com.example.plugd.ui.screens.home.FilterScreen
import com.example.plugd.ui.screens.home.AboutHelpPage
import com.example.plugd.ui.screens.plug.AddPlugScreen
import com.example.plugd.ui.screens.activity.ActivityScreen
import com.example.plugd.ui.screens.profile.ProfileScreen
import com.example.plugd.ui.screens.settings.SettingsScreen
import com.example.plugd.remote.api.RetrofitInstance
import com.example.plugd.data.remoteFireStore.EventRemoteDataSource
import com.example.plugd.data.remoteFireStore.UserRemoteDataSource
import com.example.plugd.data.repository.EventRepository
import com.example.plugd.repository.UserRepository
import com.example.plugd.viewmodels.factory.EventViewModelFactory
import com.example.plugd.viewmodels.factory.ProfileViewModelFactory
import com.example.plugd.viewmodels.EventViewModel
import com.example.plugd.data.localRoom.database.AppDatabase
import com.example.plugd.remote.firebase.FirebaseAuthService
import com.example.plugd.ui.auth.AuthViewModel
import com.example.plugd.ui.auth.AuthViewModelFactory
import com.example.plugd.ui.screens.plug.PlugDetailsScreen
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.getValue
import com.example.plugd.ui.screens.profile.ProfileViewModel

@Composable
fun AppNavHost(startDestination: String = Routes.REGISTER) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()

    // --- Local DB + Remote DataSources ---
    val userDao = AppDatabase.getInstance(context).userDao()
    val userRemote = UserRemoteDataSource()
    val userRepository = UserRepository(userDao, userRemote)

    // --- Profile ViewModel ---
    val profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(userRepository)
    )

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(FirebaseAuthService(), userDao)
    )

    // --- Event ViewModel ---
    val eventDao = AppDatabase.getInstance(context).eventDao()
    val eventRemote = EventRemoteDataSource(RetrofitInstance.api)
    val eventRepository = EventRepository(eventDao, eventRemote)

    val eventViewModel: EventViewModel = viewModel(
        factory = EventViewModelFactory(eventRepository)
    )

    // --- Google SSO Client ---
    val googleAuthClient = remember { GoogleAuthUiClient(context) }

    val loggedInUserId = profileViewModel.user.value?.userId ?: "unknown"

    NavHost(navController = navController, startDestination = startDestination) {

        // --- Register Screen ---
        composable(Routes.REGISTER) {
            RegisterScreen(navController = navController)
        }

        // --- Login Screen ---
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

        // --- Home Screen ---
        composable(Routes.HOME) {
            MainScreenWithBottomNav(
                navController = navController,
                topBar = { HomeTopBar(navController) },
                content = { padding ->
                    HomeScreen(
                        navController = navController,
                        eventViewModel = eventViewModel
                    )
                },
                loggedInUserId = loggedInUserId
            )
        }

        // --- Community Screen ---
        composable(Routes.COMMUNITY) {
            MainScreenWithBottomNav(
                navController = navController,
                topBar = { CommunityTopBar(navController) },
                content = { padding -> CommunityScreen(navController = navController) },
                loggedInUserId = loggedInUserId
            )
        }

        // --- Add Plug Screen ---
        composable(Routes.ADD) {
            AddPlugScreen(
                navController = navController,
                eventViewModel = eventViewModel,
                currentUserId = loggedInUserId  // pass it here
            )
        }

        composable(
            route = "${Routes.PLUG_DETAILS}/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Safely get the eventId argument
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""

            // Collect StateFlow as Compose state
            val events by eventViewModel.events.collectAsState()

            // Find the specific event
            val event = events.find { it.eventId == eventId }

            // Only show details if event exists
            event?.let {
                PlugDetailsScreen(navController = navController, event = it)
            }
        }

        // --- Activity Screen ---
        composable(Routes.ACTIVITY) {
            MainScreenWithBottomNav(
                navController = navController,
                topBar = { ActivityTopBar(navController) },
                content = { padding -> ActivityScreen(navController = navController) },
                loggedInUserId = loggedInUserId
            )
        }

        // --- Profile Screen ---
        composable(
            route = Routes.PROFILE
        ) {
            MainScreenWithBottomNav(
                navController = navController,
                topBar = { ProfileTopBar(navController = navController) },
                content = { padding ->
                    ProfileScreen(
                        navController = navController,
                        viewModel = profileViewModel
                    )
                },
                loggedInUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
            )
        }

        // --- Settings Screen ---
        composable(Routes.SETTINGS) {
            SettingsScreen(
                navController = navController,
                usernameInit = profileViewModel.user.value?.username ?: "",
                emailInit = profileViewModel.user.value?.email ?: "",
                onUpdateProfileField = { field, value ->
                    val user = profileViewModel.user.value
                    if (user != null) {
                        val updatedUser = when (field) {
                            "username" -> user.copy(username = value)
                            "email" -> user.copy(email = value)
                            "bio" -> user.copy(bio = value)
                            else -> user
                        }
                    }
                },
                onSignOut = { FirebaseAuth.getInstance().signOut() }
            )
        }

            // --- Filter Screen ---
            composable(Routes.FILTER) {
                FilterScreen(navController = navController)
            }

            // --- About / Support ---
            composable(Routes.ABOUT_SUPPORT) {
                AboutHelpPage(navController = navController)
            }
        }
    }


