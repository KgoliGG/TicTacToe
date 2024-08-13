package com.shrijal.tictactoe

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
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
                    }
                ) {
                    Text("Reset")
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
            .background(Color.LightGray)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = "Player 1: $wincountplayer1",
                fontSize = 22.sp
            )
            Text(
                text = "Player 2: $wincountplayer2",
                fontSize = 22.sp
            )
            Text(
                text = "Draws: $drawCount", // Display draw count
                fontSize = 22.sp
            )
        }
        Spacer(
            modifier = Modifier
                .height(40.dp)
        )

        // Display current Player when X
        if (currentPlayer == "X") {
            Text(
                text = "Current Player: $currentPlayer",
                fontSize = 24.sp,
                modifier = Modifier.height(30.dp)
            )
        } else {
            Text(
                text = "",
                fontSize = 24.sp,
                modifier = Modifier.height(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Game Board
        for (i in 0..2) {
            Row {
                for (j in 0..2) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.White)
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
                                            dialogMessage = "Player ${if (winningPlayer == "X") 1 else 2} Wins!"
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
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = board[i][j],
                            fontSize = 50.sp,
                            color = if (board[i][j] == "X") Color.Red else Color.Blue
                        )
                    }
                    Spacer(modifier = Modifier.size(2.dp))
                }
            }
            Spacer(modifier = Modifier.size(2.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Display current Player when X
        if (currentPlayer != "X") {
            Text(
                text = "Current Player: $currentPlayer",
                fontSize = 24.sp,
                modifier = Modifier.height(30.dp))
        } else {
            Text(
                text = "",
                fontSize = 24.sp,
                modifier = Modifier.height(30.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Reset button
        Button(onClick = {
            reset(board, player1, player2, emptyCells) {
                newActiveUser ->
                activeUser = newActiveUser
            }
            winner = ""
            showDialog = false
        }) {
            Text("Reset")
        }
    }
}

@Preview
@Composable
fun OfflineMultiplayerPreview() {
    TicTacToeGameOfflineMultiplayer()
}