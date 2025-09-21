package com.example.plugd.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plugd.R
import com.example.plugd.ui.navigation.Routes

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int
)

@Composable
fun OnboardingScreen(navController: NavHostController) {
    val pages = listOf(
        OnboardingPage(
            "DISCOVER NEW PLUGS",
            "Explore upcoming and recommended artists, events, and opportunities nearby.",
            R.drawable.onboarding_discover
        ),
        OnboardingPage(
            "PLUG FRIENDS INTO YOUR WORLD",
            "Share your events or creative work with your friends and followers, connect instantly.",
            R.drawable.onboarding_friends
        ),
        OnboardingPage(
            "STAY PLUGGED IN THE CURRENT",
            "Get real-time updates, notifications, and recommendations from events and artists you love.",
            R.drawable.onboarding_current
        ),
        OnboardingPage(
            "PLUG IN TO THE COMMUNITY",
            "Join a network of artists, fans, and event organisers. Find your vibrant community and find your purpose.",
            R.drawable.onboarding_community
        ),
        OnboardingPage(
            "READY TO PLUG IN?",
            "Jump straight into the PLUGD app and start connecting!",
            R.drawable.onboarding_ready
        )
    )

    val pagerState = rememberPagerState(initialPage = 0)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            pageCount = pages.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(page = pages[page])
        }

        // Custom pager indicator
        SimplePagerIndicator(currentPage = pagerState.currentPage, pageCount = pages.size)

        // Last page button
        if (pagerState.currentPage == pages.lastIndex) {
            Button(
                onClick = {
                    navController.navigate(Routes.REGISTER) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Sign Up")
            }
        }
    }
}

@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = page.imageRes),
            contentDescription = page.title,
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 24.dp)
        )
        Text(page.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Text(page.description, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun SimplePagerIndicator(currentPage: Int, pageCount: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        for (i in 0 until pageCount) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = if (i == currentPage) Color.Black else Color.LightGray,
                        shape = CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}