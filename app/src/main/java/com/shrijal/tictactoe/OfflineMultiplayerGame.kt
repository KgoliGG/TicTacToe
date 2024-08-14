package com.shrijal.tictactoe

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.shrijal.tictactoe.composable.CurrentPlayerText
import com.shrijal.tictactoe.ui.theme.*
import kotlin.random.Random
import kotlin.system.exitProcess

@Composable
fun TicTacToeGameOfflineMultiplayer() {
    var board by remember { mutableStateOf(Array(3) { Array(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf("") }
    var wincountplayer1 by remember { mutableIntStateOf(0) }
    var wincountplayer2 by remember { mutableIntStateOf(0) }
    var activeUser by remember { mutableIntStateOf(Random.nextInt(1, 3)) }
    var player1 by remember { mutableStateOf(arrayListOf<Int>()) }
    var player2 by remember { mutableStateOf(arrayListOf<Int>()) }
    var emptyCells by remember { mutableStateOf(arrayListOf<Int>()) }
    var drawCount by remember { mutableIntStateOf(0) }
//    var playerTurn by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()

    // Show dialog when the game ends with a winner
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Game Over"
                )
            },
            text = {
                Text(
                    text = dialogMessage
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        reset(board, player1, player2, emptyCells) {
                                newActiveUser ->
                            activeUser = newActiveUser
                        }
                        winner = ""
                        showDialog = false
                    },
                ) {
                    Text("Play Again")
                }
            },
            dismissButton = {
                Button(onClick = { exitProcess(1) }) {
                    Text("Exit")
                }
            }
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
            .padding(vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        //Logo Design
        Text(
            text = "Tic-Tac-Toe",
            style = TextStyle(
                fontFamily = montserrat,
                fontWeight = FontWeight(800),
                fontSize = 40.sp,
                color = Color.White,
            )
        )
        Spacer(modifier = Modifier.height(50.dp))
        // Display current Player when X
        when (currentPlayer == "X") {
            true -> CurrentPlayerText(currentPlayer = currentPlayer)
            false -> CurrentPlayerText(currentPlayer = "")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Game Board
        for (i in 0..2) {
            Row {
                for (j in 0..2) {
                    val scale by animateFloatAsState(
                        targetValue = if (board[i][j].isNotEmpty()) 1f else 0f,
                        animationSpec = tween(durationMillis = 300)
                    )
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(Deactivated)
                            .clickable {
                                if (board[i][j].isEmpty() && winner.isEmpty()) {
                                    board[i][j] = currentPlayer
                                    currentPlayer = if (currentPlayer == "X") "O" else "X"
                                    winner = checkWinner(board,
                                        onWin = { winningPlayer ->
                                            if (winningPlayer == "X") {
                                                wincountplayer1++
                                            } else {
                                                wincountplayer2++
                                            }
                                            dialogMessage =
                                                "Player ${if (winningPlayer == "X") 1 else 2} Wins!"
                                            showDialog = true
                                        },
                                        onDraw = {
                                            dialogMessage = "It's a Draw!"
                                            showDialog = true
                                            drawCount++
                                        }
                                    )
                                }
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (board[i][j].isNotEmpty()) {
                            Text(
                                text = board[i][j],
                                style = TextStyle(
                                    fontFamily = montserrat,
                                    fontWeight = FontWeight(600),
                                    fontSize = 60.sp,
                                ),
                                modifier = Modifier.scale(scale),
                                color = if (board[i][j] == "X") Secondary else Tertiary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(5.dp))
                }
            }
            Spacer(modifier = Modifier.size(5.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Display current Player when O
        when (currentPlayer != "X") {
            true -> CurrentPlayerText(currentPlayer = currentPlayer)
            false -> CurrentPlayerText(currentPlayer = "")
        }

        Spacer(modifier = Modifier.height(20.dp))

        //Scoreboard
        Column(
            modifier = Modifier
                .fillMaxWidth(.9f)
                .fillMaxHeight(.3f),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Text(
                    text = "Player 1: $wincountplayer1",
                    style = TextStyle(
                        fontFamily = montserrat,
                        fontWeight = FontWeight(400),
                        fontSize = 18.sp,
                        color = Color.White,
                    )
                )
                Text(
                    text = "Player 2: $wincountplayer2",
                    style = TextStyle(
                        fontFamily = montserrat,
                        fontWeight = FontWeight(400),
                        fontSize = 18.sp,
                        color = Color.White,
                    )
                )
            }
            Text(
                text = "Draws: $drawCount", // Display draw count
                style = TextStyle(
                    fontFamily = montserrat,
                    fontWeight = FontWeight(400),
                    fontSize = 18.sp,
                    color = Color.White,
                )
            )
        }
        Spacer(
            modifier = Modifier
                .height(60.dp)
        )

        // End Game Button
        Button(
            onClick = {
                exitProcess(1)
                //Need to change
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Secondary
            ),
            modifier = Modifier
                .width(50.dp)
                .height(50.dp),
        ) {
            Text(
                text = "X",
                style = TextStyle(
                    fontFamily = montserrat,
                    fontSize = 20.sp,
                    fontWeight = FontWeight(600),
                    color = Primary
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}



@Preview
@Composable
fun OfflineMultiplayerPreview() {
    TicTacToeGameOfflineMultiplayer()
}