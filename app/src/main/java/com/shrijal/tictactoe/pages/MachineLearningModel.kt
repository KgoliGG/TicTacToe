package com.shrijal.tictactoe.pages

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.shrijal.tictactoe.IN_PROGRESS
import com.shrijal.tictactoe.models.findBestMove
import com.shrijal.tictactoe.checkWinner
import com.shrijal.tictactoe.composable.CurrentPlayerText
import com.shrijal.tictactoe.composable.ReturntoMainMenu
import com.shrijal.tictactoe.composable.Scoreboard
import com.shrijal.tictactoe.dialogs.ShowDialogBox
import com.shrijal.tictactoe.navigation.Screens
import com.shrijal.tictactoe.ui.theme.*
import com.shrijal.tictactoe.reset
import kotlin.random.Random

@Composable
fun MachineLearningModel(navController: NavController) {
    var board by remember { mutableStateOf(Array(3) { CharArray(3) { ' ' } }) } // Use CharArray for board
    var currentPlayer by remember { mutableStateOf('X') }
    var winner by remember { mutableStateOf(IN_PROGRESS) }
    var wincountPlayer1 by remember { mutableIntStateOf(0) }
    var wincountPlayer2 by remember { mutableIntStateOf(0) }
    var activeUser by remember { mutableIntStateOf(Random.nextInt(1, 3)) }
    var player1 by remember { mutableStateOf(arrayListOf<Int>()) }
    var player2 by remember { mutableStateOf(arrayListOf<Int>()) }
    var emptyCells by remember { mutableStateOf(arrayListOf<Int>()) }
    var drawCount by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    // Show dialog when the game ends
    ShowDialogBox(
        showDialog = showDialog,
        dialogMessage = dialogMessage,
        onDismiss = {
            showDialog = false
        },
        onPlayAgain = {
            reset(board, player1, player2, emptyCells) { newActiveUser ->
                activeUser = newActiveUser
            }
            winner = IN_PROGRESS
            showDialog = false
        },
        onExit = {
            navController.navigate(route = Screens.LandingPage.name)
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
            .padding(vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Game Title
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
            text = "Offline Multiplayer".uppercase(),
            style = TextStyle(
                fontFamily = montserrat,
                fontWeight = FontWeight(400),
                fontSize = 16.sp,
                color = Color.White,
                letterSpacing = 2.sp,
            )
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Display current player text
        if (winner == IN_PROGRESS) {
            CurrentPlayerText(currentPlayer = currentPlayer.toString())
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Game board layout
        for (i in 0..2) {
            Row {
                for (j in 0..2) {
                    val scale by animateFloatAsState(
                        targetValue = if (board[i][j] != ' ') 1f else 0f,
                        animationSpec = tween(durationMillis = 300), label = ""
                    )
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                color = when (board[i][j]) {
                                    'X' -> SecondaryActivated
                                    'O' -> TertiaryActivated
                                    else -> Deactivated
                                }
                            )
                            .clickable {
                                if (board[i][j] == ' ' && winner == IN_PROGRESS) {
                                    board[i][j] = currentPlayer
                                    winner = checkWinner(
                                        board,
                                        onWin = { winningPlayer ->
                                            dialogMessage = "Player $winningPlayer Wins!"
                                            if (winningPlayer == 'X') {
                                                wincountPlayer1++
                                            } else {
                                                wincountPlayer2++
                                            }
                                            showDialog = true
                                        },
                                        onDraw = {
                                            dialogMessage = "It's a Draw!"
                                            drawCount++
                                            showDialog = true
                                        }
                                    )
                                    if (winner == IN_PROGRESS) {
                                        currentPlayer = if (currentPlayer == 'X') 'O' else 'X'

                                        // Let AI make a move if it's O's turn
                                        if (currentPlayer == 'O') {
                                            val bestMove = findBestMove(board)
                                            board[bestMove.row][bestMove.col] = 'O'
                                            winner = checkWinner(
                                                board,
                                                onWin = { winningPlayer ->
                                                    dialogMessage = "Player $winningPlayer Wins!"
                                                    wincountPlayer2++
                                                    showDialog = true
                                                },
                                                onDraw = {
                                                    dialogMessage = "It's a Draw!"
                                                    drawCount++
                                                    showDialog = true
                                                }
                                            )
                                            currentPlayer = 'X' // Switch back to player
                                        }
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (board[i][j] != ' ') {
                            Text(
                                text = board[i][j].toString(),
                                style = TextStyle(
                                    fontFamily = montserrat,
                                    fontWeight = FontWeight(600),
                                    fontSize = 60.sp,
                                ),
                                modifier = Modifier.scale(scale),
                                color = if (board[i][j] == 'X') Secondary else Tertiary
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
        when (currentPlayer != IN_PROGRESS) {
            true -> CurrentPlayerText(currentPlayer = currentPlayer.toString())
            false -> CurrentPlayerText(currentPlayer = "")
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Scoreboard
        Scoreboard(
            wincountplayer1 = wincountPlayer1,
            wincountplayer2 = wincountPlayer2,
            drawCount = drawCount
        )

        Spacer(modifier = Modifier.height(50.dp))

        // End game button
        ReturntoMainMenu(navController = navController)
    }
}

@Preview
@Composable
fun MachineLearningModelPreview() {
    MachineLearningModel(rememberNavController())
}
