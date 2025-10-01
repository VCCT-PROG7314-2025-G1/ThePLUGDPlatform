package com.example.plugd.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.plugd.ui.screens.nav.CommunityTopBar
import com.example.plugd.ui.theme.Telegraf
import com.example.plugd.viewmodels.ChatViewModel
import com.example.plugd.model.Channel
import com.example.plugd.model.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    navController: NavHostController,
    chatViewModel: ChatViewModel,
    myPosts: List<Post> // feed posts
) {
    // Load channels and user communities
    val allChannels by chatViewModel.channels.collectAsState()
    val userCommunities by chatViewModel.userJoinedChannels.collectAsState()

    Scaffold(
        topBar = { CommunityTopBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "THE PLUG COMMUNITY",
                style = MaterialTheme.typography.displayLarge.copy(fontFamily = Telegraf)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // All Channels
            Text(
                text = "All Channels",
                style = MaterialTheme.typography.headlineMedium
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth().heightIn(max = 200.dp)
            ) {
                items(allChannels) { channel ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("chat/${channel.id}/${channel.name}")
                            }
                            .padding(vertical = 8.dp)
                    ) {
                        Text(channel.name, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // My Feed & My Communities
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "My Feed", style = MaterialTheme.typography.headlineMedium)
                Text(text = "My Communities", style = MaterialTheme.typography.headlineMedium)
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                // My Feed
                LazyColumn(
                    modifier = Modifier.weight(1f).heightIn(max = 200.dp)
                ) {
                    items(myPosts) { post ->
                        Column(modifier = Modifier.padding(4.dp)) {
                            Text("${post.channelName}: ${post.content}")
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                // My Communities
                LazyColumn(
                    modifier = Modifier.weight(1f).heightIn(max = 200.dp)
                ) {
                    items(userCommunities) { channel ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("chat/${channel.id}/${channel.name}")
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Text(channel.name)
                        }
                    }
                }
            }
        }
    }
}