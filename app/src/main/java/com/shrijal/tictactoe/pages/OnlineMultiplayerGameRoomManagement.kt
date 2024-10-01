package com.shrijal.tictactoe.pages

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shrijal.tictactoe.composable.GameModeTitle
import com.shrijal.tictactoe.composable.GameTitle
import com.shrijal.tictactoe.composable.ReturntoMainMenu
import com.shrijal.tictactoe.composable.codeCheck
import com.shrijal.tictactoe.firebase.createGameCode
import com.shrijal.tictactoe.firebase.joinGameCode
import com.shrijal.tictactoe.navigation.Screens
import com.shrijal.tictactoe.ui.theme.Primary
import com.shrijal.tictactoe.ui.theme.Tertiary
import com.shrijal.tictactoe.ui.theme.TertiaryActivated
import com.shrijal.tictactoe.ui.theme.montserrat

val database = FirebaseDatabase.getInstance().reference.child("codes")

class OnlineMultiplayerGameRoomManagement : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        setContent {
            GameRoomManagement(
                rememberNavController(),
                database
            )
        }
    }
}


@Composable
fun GameRoomManagement(navController: NavController, database: DatabaseReference){
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var code by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    var showToast by remember { mutableStateOf<String?>(null) }

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
                .height(50.dp)
        )

        // Game UI Design
        GameTitle()

        GameModeTitle(text = "Online Multiplayer Game")

        Spacer(
            modifier = Modifier
                .weight(.5f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(.6f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Enter a Username",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontFamily = montserrat,
                    fontWeight = FontWeight(400),
                    fontSize = 16.sp,
                    color = Color.White,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Text-field to enter Username
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
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

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Enter Room Code",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontFamily = montserrat,
                    fontWeight = FontWeight(400),
                    fontSize = 16.sp,
                    color = Color.White,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            // TextField to enter Room Code
            OutlinedTextField(
                value = code,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange =  {
                    if (it.text.length <= 6){
                        code = it
                    }
                },
                shape = RoundedCornerShape(100.dp),
                modifier = Modifier.fillMaxWidth(),
//                label = { Text("Enter Code") },
                placeholder = {
                    Text(
                        text = "123456",
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

            Spacer(modifier = Modifier.height(20.dp))

            // Error Message
            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Tertiary)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Create Game
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .border(1.dp, TertiaryActivated, RoundedCornerShape(100.dp))
                    .height(50.dp)
                    .fillMaxWidth(),
                onClick = {
                    if (codeCheck(code.text)){
                        createGameCode(
                            database,
                            username.text,
                            code.text,
                            onError = { errorMessage = it },
                            onSuccess = {
                                navController.navigate(Screens.OnlineMultiplayerGame.route
                                    .replace("{username}", username.text)
                                    .replace("{gameCode}", code.text)
                                ){
                                    launchSingleTop = true
                                }
                                showToast = "Room Created and Joined Successfully"
                            }
                        )
                    }
                    else {
                        errorMessage = "Code must be of 6-Digits"
                    }
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
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Join Game
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .border(1.dp, TertiaryActivated, RoundedCornerShape(100.dp))
                    .height(50.dp)
                    .fillMaxWidth(),
                onClick = {
                    if (codeCheck(code.text)){
                        joinGameCode(
                            database,
                            username.text,
                            code.text,
                            onError = { errorMessage = it },
                            onSuccess = {
                                navController.navigate(Screens.OnlineMultiplayerGame.route
                                    .replace("{username}", username.text)
                                    .replace("{gameCode}", code.text)
                                ){
                                    launchSingleTop = true
                                }
                                showToast = "Joined Successfully!"
                            },
                            onRoomFull = { errorMessage = "Room is full!" },
                            onReconnectionAllowed = {
                                navController.navigate(Screens.OnlineMultiplayerGame.route
                                    .replace("{username}", username.text)
                                    .replace("{gameCode}", code.text)
                                ){
                                    launchSingleTop = true
                                }
                                showToast = "Reconnected to the game successfully"
                            }
                        )
                    }
                    else {
                        errorMessage = "Code must be of 6-Digits"
                    }
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
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(
            modifier = Modifier
                .weight(.5f)
        )

        // Return to Main Menu
        ReturntoMainMenu(navController = navController)
    }

    // Show Toast when reconnection is allowed
    showToast?.let {
        LaunchedEffect(it) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            showToast = null
        }
    }
}


@Preview
@Composable
fun SetUserNamePreview(){
    GameRoomManagement(
        rememberNavController(),
        database)
}