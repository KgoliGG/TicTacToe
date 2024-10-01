//package com.shrijal.tictactoe.composable
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.animation.core.*
//import androidx.compose.foundation.*
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//import androidx.compose.ui.draw.*
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.*
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.*
//import androidx.navigation.NavController
//import androidx.navigation.compose.rememberNavController
//import com.google.firebase.database.DatabaseReference
//import com.shrijal.tictactoe.composable.checkWinner
//import com.shrijal.tictactoe.composable.CurrentPlayerText
//import com.shrijal.tictactoe.composable.GameModeTitle
//import com.shrijal.tictactoe.composable.GameTitle
//import com.shrijal.tictactoe.composable.ReturntoMainMenu
//import com.shrijal.tictactoe.composable.Scoreboard
//import com.shrijal.tictactoe.dialogs.ShowDialogBox
//import com.shrijal.tictactoe.navigation.Screens
//import com.shrijal.tictactoe.composable.reset
//import com.shrijal.tictactoe.ui.theme.*
//import kotlin.random.Random
//
//
//@Composable
//fun GameBoard(
//    gameCode: String,
//    currentPlayer: String,
//    database: DatabaseReference
//) {
//    var board by remember { mutableStateOf(List(9) { "" }) }
//    var currentTurn by remember { mutableStateOf("player1") }
//    var winner by remember { mutableStateOf("") }
//
//    // Firebase listener for real-time updates
//    LaunchedEffect(gameCode) {
//        database.child(gameCode).get().addOnSuccessListener { snapshot ->
//            board = (snapshot.child("board").value as List<String>?) ?: List(9) { "" }
//            currentTurn = snapshot.child("currentTurn").value as String
//            winner = snapshot.child("winner").value as String
//        }
//    }
//
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(text = if (winner.isNotEmpty()) "$winner wins!" else "Turn: $currentTurn")
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        // Display the game board
//        for (i in 0..2) {
//            Row {
//                for (j in 0..2) {
//                    val index = i * 3 + j
//                    Box(
//                        modifier = Modifier
//                            .size(100.dp)
//                            .border(2.dp, Color.Black)
//                            .clickable {
//                                if (board[index].isEmpty() && currentPlayer == currentTurn && winner.isEmpty()) {
//                                    makeMove(database, gameCode, index, currentPlayer)
//                                }
//                            },
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(text = board[index], style = TextStyle(fontSize = 36.sp))
//                    }
//                }
//            }
//        }
//    }
//}