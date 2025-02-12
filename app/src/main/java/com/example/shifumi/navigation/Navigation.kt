package com.example.shifumi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shifumi.ui.screens.GameScreen
import com.example.shifumi.ui.screens.HomeScreen

object Destinations {
    const val Home = "home"
    const val Game = "game"
}

@Composable
fun ShifumiNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.Home,
        modifier = modifier
    ) {
        composable(Destinations.Home) {
            HomeScreen(
                onPlayClicked = {
                    navController.navigate(Destinations.Game)
                }
            )
        }
        composable(Destinations.Game) {
            GameScreen()
        }
    }
}
