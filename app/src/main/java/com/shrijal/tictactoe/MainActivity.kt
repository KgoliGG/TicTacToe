package com.shrijal.tictactoe

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.shrijal.tictactoe.ui.theme.TicTacToeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme  {
//                Menu()
                TicTacToeGameOfflineMultiplayer()
            }
        }
    }
}

@Composable
fun Menu(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tic-Tac-Toe",
            fontSize = 30.sp
        )

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth(.5f),
            onClick = {
            /*TODO*/
            }
        ) {
            Text(
                text = "Play with AI",
                fontSize = 14.sp
            )

        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth(.5f),
            onClick = {
            /*TODO*/
            }
        ) {
            Text(
                text = "Local Multiplayer",
                fontSize = 14.sp
            )

        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth(.5f),
            onClick = {
            /*TODO*/
            }
        ) {
            Text(
                text = "Online Multiplayer",
                fontSize = 14.sp
            )

        }
    }
}


@Preview
@Composable
fun MenuPreview() {
    Menu()
}
