package com.shrijal.tictactoe.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.shrijal.tictactoe.pages.*

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    // Initialize Firebase Realtime Database
    val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    NavHost(
        navController = navController,
        startDestination = Screens.LandingPage.name
    ) {
        composable(route = Screens.LandingPage.name) {
            LandingPage(navController = navController)
        }
        composable(route = Screens.MachineLearningModel.name) {
            MachineLearningModel(navController = navController)
        }
        composable(route = Screens.OfflineMultiplayerGame.name) {
            OfflineMultiplayer(navController = navController)
        }

//        composable(route = Screens.OnlineMultiplayerGame.name) { backStackEntry ->
//            val gameCode = backStackEntry.arguments?.getString("gameCode") ?: ""
//            val username = backStackEntry.arguments?.getString("username") ?: ""
//            OnlineMultiplayerGame(
//                navController = navController,
//                gameCode = gameCode,
//                username = username,
//                database = database
//            )
//        }

        composable(
            route = Screens.OnlineMultiplayerGame.route,
            arguments = listOf(
                navArgument("username") { type = NavType.StringType },
                navArgument("gameCode") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val gameCode = backStackEntry.arguments?.getString("gameCode") ?: ""
            val username = backStackEntry.arguments?.getString("username") ?: ""

            OnlineMultiplayerGame(
                navController = navController,
                gameCode = gameCode,
                currentPlayer = username,
                database = database
            )
        }


        composable(route = Screens.GameRoomManagement.name) {
            GameRoomManagement(navController = navController, database = database) // Pass DatabaseReference
        }
    }
}
