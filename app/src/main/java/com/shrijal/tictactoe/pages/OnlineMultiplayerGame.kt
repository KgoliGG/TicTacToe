package com.shrijal.tictactoe.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.google.firebase.database.*
import com.shrijal.tictactoe.composable.ReturntoMainMenu
import com.shrijal.tictactoe.firebase.*
import com.shrijal.tictactoe.ui.theme.Primary
import com.shrijal.tictactoe.ui.theme.Tertiary
import com.shrijal.tictactoe.ui.theme.TertiaryActivated
import com.shrijal.tictactoe.ui.theme.montserrat
import kotlinx.coroutines.*

@Composable
fun OnlineMultiplayerUI(navController: NavController) {
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
        Spacer(modifier = Modifier.height(8.dp))
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
        Spacer(modifier = Modifier.height(100.dp))

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
        } else {
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
                    createGameCode(database, code.text, onError = {
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

            Spacer(modifier = Modifier.height(8.dp))

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
                joinGameCode(database, code.text, onError = {
                    isLoading = false
                    errorMessage = it
                }, onSuccess = {
                    isLoading = false
                    errorMessage = "Joined the game successfully!"
                })
            }) {
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
            ReturntoMainMenu(navController = navController)
        }
    }
}

@Preview
@Composable
fun OnlineMultiplayerUIPreview(){
    OnlineMultiplayerUI(rememberNavController())
}