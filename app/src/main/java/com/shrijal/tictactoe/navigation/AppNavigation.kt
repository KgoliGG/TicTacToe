package com.shrijal.tictactoe.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shrijal.tictactoe.pages.*

@Composable
fun AppNavigation(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screens.LandingPage.name
    ) {
        composable(route = Screens.LandingPage.name) {
            LandingPage(navController = navController)
        }
        composable(Screens.MachineLearningModel.name) {
            MachineLearningModel(navController = navController)
        }
        composable(route = Screens.OfflineMultiplayerGame.name) {
            OfflineMultiplayerGame(navController = navController)
        }
        composable(Screens.OnlineMultiplayerGame.name) {
//            OnlineMultiplayerGame(navController = navController)
        }
    }
}