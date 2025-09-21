package com.example.plugd.ui.utils

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

/**
 * Use this to pass a NavController in previews
 */
@Composable
fun PreviewNavController(): NavHostController {
    return rememberNavController()
}