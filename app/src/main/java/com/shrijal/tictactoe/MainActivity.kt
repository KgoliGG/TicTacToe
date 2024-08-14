package com.shrijal.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.shrijal.tictactoe.Pages.Menu
import com.shrijal.tictactoe.Pages.TicTacToeGameOfflineMultiplayer
import com.shrijal.tictactoe.ui.theme.TicTacToeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme  {
                Menu()
//                TicTacToeGameOfflineMultiplayer()
            }
        }
    }
}

