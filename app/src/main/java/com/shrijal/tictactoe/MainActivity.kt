package com.shrijal.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.shrijal.tictactoe.ui.theme.*
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

@Composable
fun Menu(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Tertiary, Secondary), // Replace with your desired colors
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(
            modifier = Modifier
                .height(100.dp)
        )

        Text(
            text = "Tic-Tac-Toe",
            style = TextStyle(
                fontFamily = montserrat,
                fontWeight = FontWeight(800),
                fontSize = 50.sp,
                color = Color.White,
            )
        )

        Text(
            text = "Machine Learning Model".uppercase(),
            style = TextStyle(
                fontFamily = montserrat,
                fontWeight = FontWeight(600),
                fontSize = 16.sp,
                color = Color.White,
                letterSpacing = 2.sp,

                )
        )

        Spacer(
            modifier = Modifier
                .height(300.dp)
        )

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            ),
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(.6f),
            onClick = {
            /*TODO*/
            }

        ) {
            Text(
                text = "Play with AI",
                style = TextStyle(
                    fontFamily = montserrat,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )

        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            ),
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(.6f),
            onClick = {
            /*TODO*/
            }
        ) {
            Text(
                text = "Local Multiplayer",
                style = TextStyle(
                    fontFamily = montserrat,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )

        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            ),
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(.6f),
            onClick = {
            /*TODO*/
            }
        ) {
            Text(
                text = "Online Multiplayer",
                style = TextStyle(
                    fontFamily = montserrat,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )

        }

        Spacer(
            modifier = Modifier
                .height(100.dp)
        )

        Text(
            text = "Shrijal Khayargoli & Oken Shrestha",
            style = TextStyle(
                fontFamily = montserrat,
                fontSize = 14.sp,
                color = Primary,
                fontWeight = FontWeight(400),
            )
        )
    }
}


@Preview
@Composable
fun MenuPreview() {
    Menu()
}
