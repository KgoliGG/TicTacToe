package com.shrijal.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.shrijal.tictactoe.ui.theme.TicTacToeTheme
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme  {
                TicTacToeGame()
            }
        }
    }
}

@Preview
@Composable
fun UIPreview() {
    TicTacToeGame()
}
