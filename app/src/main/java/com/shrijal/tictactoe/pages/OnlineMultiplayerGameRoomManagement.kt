package com.shrijal.tictactoe.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
fun GameRoomManagement(navController: NavController){
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var code by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    val database = FirebaseDatabase.getInstance().reference.child("codes")



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
            .padding(vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){

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
                .weight(1f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(.6f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ){
            Text(
                text = "Enter a Username",
                modifier = Modifier
                    .fillMaxWidth(),
                style = TextStyle(
                    fontFamily = montserrat,
                    fontWeight = FontWeight(400),
                    fontSize = 16.sp,
                    color = Color.White,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(
                modifier = Modifier
                    .height(2.dp)
            )

            //Textfield to enter Username
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier
                    .fillMaxWidth(),
//                label ={
//                    Text(
//                        text = "Username",
//                        style = TextStyle(
//                            fontFamily = montserrat,
//                            fontWeight = FontWeight(400),
//                            fontSize = 16.sp,
//                            color = Color.White,
//                            letterSpacing = 2.sp,
//                            textAlign = TextAlign.Center
//                        )
//                    )
//                },
                placeholder ={
                    Text(
                        text = "User",
                        style = TextStyle(
                            fontFamily = montserrat,
                            fontWeight = FontWeight(400),
                            fontSize = 16.sp,
                            color = Color.White,
                            letterSpacing = 2.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            )

            Spacer(
                modifier = Modifier
                    .height(20.dp)
            )

            Text(
                text = "Enter Room Code",
                modifier = Modifier
                    .fillMaxWidth(),
                style = TextStyle(
                    fontFamily = montserrat,
                    fontWeight = FontWeight(400),
                    fontSize = 16.sp,
                    color = Color.White,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(
                modifier = Modifier
                    .height(2.dp)
            )

            //TextField to enter Room Code
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier
                    .fillMaxWidth(),
//                label ={
//                    Text(
//                        text = "Enter Room Code \"XXX-XXX\"",
//                        style = TextStyle(
//                            fontFamily = montserrat,
//                            fontWeight = FontWeight(400),
//                            fontSize = 16.sp,
//                            color = Color.White,
//                            letterSpacing = 2.sp,
//                            textAlign = TextAlign.Center
//                        )
//                    )
//                },
                placeholder ={
                    Text(
                        text = "XXXX",
                        style = TextStyle(
                            fontFamily = montserrat,
                            fontWeight = FontWeight(400),
                            fontSize = 16.sp,
                            color = Color.White,
                            letterSpacing = 2.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            )

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            //Error Messagea
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

            Spacer(
                modifier = Modifier
                    .height(10.dp)
            )

            //Create Game
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary
                ),
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(100.dp)
                    )
                    .border(
                        1.dp,
                        color = TertiaryActivated,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .height(50.dp)
                    .fillMaxWidth(),
                onClick = {
                    createGameCode(
                        database,
                        username.text,
                        code.text,
                        onError = {
                            errorMessage = it
                        },
                        onSuccess = {
                            errorMessage = "Joined Successfully"
                        }
                    )
                }
            ){
                Text(
                    text = "Create Game",
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
            //Join Game
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary
                ),
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(100.dp)
                    )
                    .border(
                        1.dp,
                        color = TertiaryActivated,
                        shape = RoundedCornerShape(100.dp)
                    )
                    .height(50.dp)
                    .fillMaxWidth(),
                onClick = {
                    joinGameCode(
                        database,
                        username.text,
                        code.text,
                        onError = {
                            errorMessage = it
                        },
                        onSuccess = {
                            errorMessage = "Joined Successfully"
                        },
                        onRoomFull = {
                            errorMessage = "Room is full!"
                        },
                        onReconnectionAllowed = {
                            errorMessage = "Reconnected Successfully"
                        }
                    )

                }

            ){
                Text(
                    text = "Join Game",
                    style = TextStyle(
                        fontFamily = montserrat,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(50.dp)
        )

        //Return to Main Menu
        ReturntoMainMenu(
            navController = navController
        )
    }
}

@Preview
@Composable
fun SetUserNamePreview(){
    GameRoomManagement(rememberNavController())
}