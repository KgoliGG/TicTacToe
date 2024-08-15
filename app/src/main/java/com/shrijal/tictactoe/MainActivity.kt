package com.shrijal.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.shrijal.tictactoe.navigation.AppNavigation
import com.shrijal.tictactoe.ui.theme.TicTacToeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme  {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}

