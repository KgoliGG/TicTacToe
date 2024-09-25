package com.shrijal.tictactoe.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shrijal.tictactoe.composable.ReturntoMainMenu
import com.shrijal.tictactoe.firebase.createGameCode
import com.shrijal.tictactoe.firebase.joinGameCode
import com.shrijal.tictactoe.ui.theme.Primary
import com.shrijal.tictactoe.ui.theme.Tertiary
import com.shrijal.tictactoe.ui.theme.TertiaryActivated
import com.shrijal.tictactoe.ui.theme.montserrat

class OnlineMultiplayerGameRoomManagement : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        setContent {
            GameRoomManagement(rememberNavController())
        }
    }
}


@Composable
fun GameRoomManagement(navController: NavController) {
    var code by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(false) }
    var isCodeMaker by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }
    val database = FirebaseDatabase.getInstance().reference.child("codes")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
            .padding(vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(
            modifier = Modifier
                .height(100.dp)
        )

        //Game Text Design
        Text(
            text = "Tic-Tac-Toe",
            style = TextStyle(
                fontFamily = montserrat,
                fontWeight = FontWeight(800),
                fontSize = 40.sp,
                color = Color.White,
            )
        )

        Text(
            text = "Online Multiplayer".uppercase(),
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
                .height(100.dp)
        )

        Text(
            text = "Enter Game Code",
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
                .height(8.dp)
        )

        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            modifier = Modifier
                .fillMaxWidth(.6f)
                .padding(16.dp),
            label ={
                Text(
                    text = "XXX-XXX",
                    style = TextStyle(
                        fontFamily = montserrat,
                        fontWeight = FontWeight(400),
                        fontSize = 16.sp,
                        color = Color.White,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center
                    )
                )
            },
            placeholder ={
                Text(
                    text = "",
                    style = TextStyle(
                        fontFamily = montserrat,
                        fontWeight = FontWeight(600),
                        fontSize = 24.sp,
                        color = Color.White,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        )

        Spacer(
            modifier = Modifier
                .height(100.dp)
        )

        if (errorMessage.isNotEmpty()) {

            Spacer(
                modifier = Modifier.
                height(16.dp)
            )

            Text(
                text = errorMessage,
                color = Tertiary
            )
        }

        if (isLoading) {
            CircularProgressIndicator()
        }

        else {
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
                    isCodeMaker = true
                    isLoading = true
                    createGameCode(
                        database,
                        code.text,
                        onError = {
                            isLoading = false
                            errorMessage = it
                        },
                        onSuccess = {
                            isLoading = false
                            errorMessage = "Game code created successfully!"
                        }
                    )
                }
            ) {
                Text(
                    text = "Create Game",
                    style = TextStyle(
                        fontFamily = montserrat,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center)
            }

            Spacer(
                modifier = Modifier
                    .height(8.dp)
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
                    isCodeMaker = false
                    isLoading = true
                    joinGameCode(
                        database,
                        code.text,
                        onError = {
                            isLoading = false
                            errorMessage = it
                        },
                        onSuccess = {
                            isLoading = false
                            errorMessage = "Joined the game successfully!"
                        },
                        onRoomFull = {
                            isLoading = false
                            errorMessage = "Room is full! Please try another room."
                        }
                    )
                }
            ) {
                Text(
                    text = "Join Game",
                    style = TextStyle(
                        fontFamily = montserrat,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center)
            }

            Spacer(
                modifier = Modifier
                    .height(50.dp)
            )

            // End Game Button
            ReturntoMainMenu(
                navController = navController
            )
        }
    }
}


@Preview
@Composable
fun GameRoomManagementUI(){
    GameRoomManagement(
        rememberNavController()
    )
}