package com.shrijal.tictactoe.pages


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.shrijal.tictactoe.models.*
import com.shrijal.tictactoe.composable.checkWinner
import com.shrijal.tictactoe.composable.*
import com.shrijal.tictactoe.dialogs.ShowDialogBox
import com.shrijal.tictactoe.navigation.Screens
import com.shrijal.tictactoe.ui.theme.*
import com.shrijal.tictactoe.composable.reset
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun MachineLearningModel(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isAIMoving by remember { mutableStateOf(false) }
    var qTable by remember { mutableStateOf(loadQTable(context)) }
    var board by remember { mutableStateOf(Array(3) { CharArray(3) { ' ' } }) }
    var currentPlayer by remember { mutableStateOf(if (Random.nextBoolean()) 'X' else 'O') }
    var winner by remember { mutableStateOf("") }
    var wincountPlayer1 by remember { mutableIntStateOf(0) }
    var wincountPlayer2 by remember { mutableIntStateOf(0) }
    var drawCount by remember { mutableIntStateOf(0) }
    var activeUser by remember { mutableIntStateOf(Random.nextInt(1, 3)) }
    var player1 by remember { mutableStateOf(arrayListOf<Int>()) }
    var player2 by remember { mutableStateOf(arrayListOf<Int>()) }
    var emptyCells by remember { mutableStateOf(arrayListOf<Int>()) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var resetGameTrigger by remember { mutableStateOf(false) }

    // Show dialog when the game ends
    ShowDialogBox(
        showDialog = showDialog,
        dialogMessage = dialogMessage,
        onDismiss = {
            showDialog = false
        },
        onPlayAgain = {
            resetGameTrigger = true
            showDialog = false
        },
        onExit = {
            saveQTable(context, qTable) // Save Q-table before exiting
            navController.navigate(route = Screens.LandingPage.name)
        }
    )

    // Automatically make the first move if the model starts
    LaunchedEffect(currentPlayer) {
        if (currentPlayer == 'O') {
            delay(1000) // 1-second delay for AI move
            val move = chooseAction(board, qTable)
            board[move.row][move.col] = 'O'
            isAIMoving = false
            currentPlayer = 'X'
        }
    }

    // LaunchedEffect to reset the game
    LaunchedEffect(resetGameTrigger) {
        if (resetGameTrigger) {
            reset(board, player1, player2, emptyCells) { newActiveUser ->
                activeUser = newActiveUser
            }
            currentPlayer = if (Random.nextBoolean()) 'X' else 'O'
            if (currentPlayer == 'O') {
                delay(1000)
                val move = chooseAction(board, qTable)
                board[move.row][move.col] = 'O'
                isAIMoving = false
                currentPlayer = 'X'
            }
            winner = ""
            resetGameTrigger = false
        }
    }

    // Function to handle AI move with delay
    fun makeAIMove() {
        scope.launch {
            delay(1000) // 1-second delay for AI's move
            val bestMove = chooseAction(board, qTable)
            board[bestMove.row][bestMove.col] = 'O'
            val lastAction = bestMove // Store the last action
            winner = checkWinner(
                board,
                onWin = { winningPlayer ->
                    dialogMessage = "Player $winningPlayer Wins!"
                    if (winningPlayer == "O") {
                        wincountPlayer2++
                        updateQValues(qTable, board, "lose", lastAction) // Update Q-values for AI losing
                    } else {
                        wincountPlayer1++
                        updateQValues(qTable, board, "win", lastAction) // Update Q-values for AI winning
                    }
                    showDialog = true
                },
                onDraw = {
                    dialogMessage = "It's a Draw!"
                    drawCount++
                    updateQValues(qTable, board, "draw", lastAction) // Update Q-values for a draw
                    showDialog = true
                }
            )
            isAIMoving = false
            currentPlayer = 'X'
        }
    }

    // Function to handle player's move
    fun handlePlayerMove(row: Int, col: Int) {
        if (board[row][col] == ' ' && winner.isEmpty()) {
            board[row][col] = currentPlayer
            winner = checkWinner(
                board,
                onWin = { winningPlayer ->
                    dialogMessage = "Player $winningPlayer Wins!"
                    if (winningPlayer == "X") {
                        wincountPlayer1++
                        updateQValues(qTable, board, "win", null) // Update Q-values for AI winning
                    } else if (winningPlayer == "O") {
                        wincountPlayer2++
                        updateQValues(qTable, board, "lose", null) // Update Q-values for AI losing
                    }
                    showDialog = true
                },
                onDraw = {
                    dialogMessage = "It's a Draw!"
                    drawCount++
                    updateQValues(qTable, board, "draw", null) // Update Q-values for a draw
                    showDialog = true
                }
            )
            if (winner.isEmpty()) {
                currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
                if (currentPlayer == 'O' && !isAIMoving) {
                    isAIMoving = true // Indicate AI is making a move
                    makeAIMove()
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
            .padding(vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(
            modifier = Modifier
                .height(50.dp)
        )

        // Game UI Design
        GameTitle()

        GameModeTitle(text = "Trained with Q-Learning Algorithm")

        Spacer(modifier = Modifier.height(50.dp))

        // Display current Player
        CurrentPlayerText(currentPlayer = currentPlayer.toString())

        Spacer(modifier = Modifier.height(20.dp))

        // Game board layout
        for (i in 0..2) {
            Row {
                for (j in 0..2) {
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
                            .clickable { handlePlayerMove(i, j) },
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

        // Scoreboard
        Scoreboard(
            wincountplayer1 = wincountPlayer1,
            wincountplayer2 = wincountPlayer2,
            drawCount = drawCount
        )

        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        // End game button
        ReturntoMainMenu(navController = navController)
    }

    // Save Q-table when the game ends
    DisposableEffect(Unit) {
        onDispose {
            saveQTable(context, qTable)
        }
    }
}


@Preview
@Composable
fun MachineLearningModelPreview() {
    MachineLearningModel(rememberNavController())
}
