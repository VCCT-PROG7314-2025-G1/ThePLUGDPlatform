package com.example.plugd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.plugd.data.hasSeenOnboarding
import com.example.plugd.data.saveOnboardingCompleted
import com.example.plugd.database.AppDatabase
import com.example.plugd.ui.navigation.Routes
import com.example.plugd.ui.screens.nav.AppNavHost
import com.example.plugd.ui.screens.onboarding.OnboardingScreen
import com.example.plugd.ui.theme.PLUGDTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        setContent {
            PLUGDTheme {
                AppNavHost()
            }
        }
    }
}