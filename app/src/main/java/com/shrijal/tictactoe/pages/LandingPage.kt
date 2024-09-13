package com.shrijal.tictactoe.pages

import android.view.ViewGroup
import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.shrijal.tictactoe.navigation.Screens
import com.shrijal.tictactoe.ui.theme.*
import kotlin.system.exitProcess


@Composable
fun LandingPage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Primary
//                brush = Brush.verticalGradient(
//                    colors = listOf(Tertiary, Secondary), // Replace with your desired colors
//                    startY = 0f,
//                    endY = Float.POSITIVE_INFINITY
//                )
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
                fontWeight = FontWeight(400),
                fontSize = 16.sp,
                color = Color.White,
                letterSpacing = 2.sp,

                )
        )

        Spacer(
            modifier = Modifier
                .height(250.dp)
        )

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            ),

            modifier = Modifier
                .height(50.dp)
                .clip(
                    shape = RoundedCornerShape(100.dp)
                )
                .border(
                    1.dp,
                    color = TertiaryActivated,
                    shape = RoundedCornerShape(100.dp)
                )
                .fillMaxWidth(.6f),
            onClick = {
                navController.navigate(Screens.MachineLearningModel.name)
            }

        ) {
            Text(
                text = "Play with AI",
                style = TextStyle(
                    fontFamily = montserrat,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
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
                .fillMaxWidth(.6f)
                .clip(
                    shape = RoundedCornerShape(100.dp)
                )
                .border(
                    1.dp,
                    color = TertiaryActivated,
                    shape = RoundedCornerShape(100.dp)
                ),
            onClick = {
                navController.navigate(Screens.OfflineMultiplayerGame.name)
            },
        ) {
            Text(
                text = "Local Multiplayer",
                style = TextStyle(
                    fontFamily = montserrat,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
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
                .fillMaxWidth(.6f)
                .clip(
                    shape = RoundedCornerShape(100.dp)
                )
                .border(
                    1.dp,
                    color = TertiaryActivated,
                    shape = RoundedCornerShape(100.dp)
                ),
            onClick = {
                navController.navigate(Screens.OnlineMultiplayerGame.name)
            }
        ) {
            Text(
                text = "Online Multiplayer",
                style = TextStyle(
                    fontFamily = montserrat,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
        }

        Spacer(
            modifier = Modifier
                .height(100.dp)
        )
        
        Button(
            onClick = {
                exitProcess(1)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary
            ),
            modifier = Modifier
                .width(100.dp)
                .height(50.dp)
                .clip(
                    shape = RoundedCornerShape(100.dp)
                )
                .border(
                    1.dp,
                    color = Tertiary,
                    shape = RoundedCornerShape(100.dp)
                ),
        ) {
            Text(
                text = "Exit",
                style = TextStyle(
                    fontFamily = montserrat,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = Color.White
                ),
                textAlign = TextAlign.Center
            )
        }

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        Text(
            text = "Shrijal Khayargoli & Oken Shrestha",
            style = TextStyle(
                fontFamily = montserrat,
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight(400),
            )
        )
    }
}


@Preview
@Composable
fun MenuPreview() {
    LandingPage(rememberNavController())
}
