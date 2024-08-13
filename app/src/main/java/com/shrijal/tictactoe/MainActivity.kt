package com.shrijal.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shrijal.tictactoe.ui.theme.TicTacToeTheme
import kotlin.random.Random
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme  {
                TicTacToeGame()
            }
        }
    }
}

@Composable
fun TicTacToeGame() {
    var board by remember { mutableStateOf(Array(3) { Array(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf("") }
    var wincountplayer1 by remember { mutableStateOf(0) }
    var wincountplayer2 by remember { mutableStateOf(0) }
    var activeUser by remember { mutableStateOf(Random.nextInt(1, 3)) }
    var player1 by remember { mutableStateOf(arrayListOf<Int>()) }
    var player2 by remember { mutableStateOf(arrayListOf<Int>()) }
    var emptyCells by remember { mutableStateOf(arrayListOf<Int>()) }
    var drawCount by remember { mutableStateOf(0) }
    var playerTurn by remember { mutableStateOf(true) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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
                        board = Array(3) { Array(3) { "" } }
                        reset(board, player1, player2, emptyCells) { newActiveUser ->
                            activeUser = newActiveUser
                        }
                        wincountplayer1 += if (dialogMessage.contains("Player 1 Wins")) 1 else 0
                        wincountplayer2 += if (dialogMessage.contains("Player 2 Wins")) 1 else 0
                        drawCount += if (dialogMessage.contains("Draw")) 1 else 0 // Increment draw count
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
                text = "Player 1 : $wincountplayer1",
                fontSize = 22.sp)
            Text(
                text = "Player 2 : $wincountplayer2",
                fontSize = 22.sp)
        }
        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        // Display current Player when X
        if (currentPlayer == "X") {
            Text(
                text = "Current Player: $currentPlayer",
                fontSize = 24.sp
            )
        } else {
            Text(text = "", fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        //Game Board
        for (i in 0..2) {
            Row() {
                for (j in 0..2) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.White)
                            .clickable {
                                if (board[i][j].isEmpty() && winner.isEmpty()) {
                                    board[i][j] = currentPlayer
                                    currentPlayer = if (currentPlayer == "X") "O" else "X"
                                    winner = checkWinner(
                                        board,
                                        setShowDialog = { showDialog = it },
                                        setDialogMessage = { dialogMessage = it }
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

        //Winner Dialog Box
        if (winner.isNotEmpty()){

        }

        // Display current Player when X
        if (currentPlayer != "X") {
            Text(text = "Current Player: $currentPlayer", fontSize = 24.sp)
        } else {
            Text(text = "", fontSize = 24.sp)
        }

        // Reset button
        Button(onClick = {
            board = Array(3) { Array(3) { "" } }
            reset(board, player1, player2, emptyCells) { newActiveUser ->
                activeUser = newActiveUser
            }
            winner = ""
        }) {
            Text("Reset")
        }
    }
}

// Check for a winner
fun checkWinner(
    board: Array<Array<String>>,
    setShowDialog: (Boolean) -> Unit,
    setDialogMessage: (String) -> Unit
): String {
    val winningCombinations = arrayOf(
        arrayOf(0, 0, 0, 1, 0, 2), arrayOf(1, 0, 1, 1, 1, 2), arrayOf(2, 0, 2, 1, 2, 2), // rows
        arrayOf(0, 0, 1, 0, 2, 0), arrayOf(0, 1, 1, 1, 2, 1), arrayOf(0, 2, 1, 2, 2, 2), // columns
        arrayOf(0, 0, 1, 1, 2, 2), arrayOf(0, 2, 1, 1, 2, 0)  // diagonals
    )

    for (combination in winningCombinations) {
        val x1 = combination[0]
        val y1 = combination[1]
        val x2 = combination[2]
        val y2 = combination[3]
        val x3 = combination[4]
        val y3 = combination[5]

        if (board[x1][y1] == board[x2][y2] && board[x2][y2] == board[x3][y3] && board[x1][y1].isNotEmpty()) {
            setShowDialog(true)
            setDialogMessage("Player ${board[x1][y1]} Wins!")
            return board[x1][y1]
        }
    }

    // Check for draw
    if (board.all { row -> row.all { it.isNotEmpty() } }) {
        setShowDialog(true)
        setDialogMessage("It's a Draw!")
    }

    return ""
}



//Reset the board
fun reset(
    board: Array<Array<String>>,
    player1: ArrayList<Int>,
    player2: ArrayList<Int>,
    emptyCells: ArrayList<Int>,
    activeUserSetter: (Int) -> Unit // Pass a function to set activeUser
) {
    for (i in 0..2) {
        for (j in 0..2) {
            board[i][j] = ""
        }
    }
    player1.clear()
    player2.clear()
    emptyCells.clear()
    // Randomly select the first player for the new game
    activeUserSetter(Random.nextInt(1, 3))
}

@Preview
@Composable
fun UIPreview() {
    TicTacToeGame()
}
