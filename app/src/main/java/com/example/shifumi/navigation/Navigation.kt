package com.example.shifumi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shifumi.ui.screens.GameScreen
import com.example.shifumi.ui.screens.GameStrategyScreen
import com.example.shifumi.ui.screens.GameVsComputerScreen
import com.example.shifumi.ui.screens.HomeScreen

object Destinations {
    const val Home = "home"
    const val GameSolo = "game_solo"
    const val GameVsComputer = "game_vs_computer"

    const val GameStrategy = "game_strategy"
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
                onPlaySoloClicked = { navController.navigate(Destinations.GameSolo) },
                onPlayVsComputerClicked = { navController.navigate(Destinations.GameVsComputer) },
                onPlayStrategyClicked = {
                    navController.navigate(Destinations.GameStrategy)
                }
            )
        }
        composable(Destinations.GameSolo) {
            GameScreen()
        }
        composable(Destinations.GameVsComputer) {
            GameVsComputerScreen()
        }
        composable(Destinations.GameStrategy) {
            GameStrategyScreen()
        }
    }
}
