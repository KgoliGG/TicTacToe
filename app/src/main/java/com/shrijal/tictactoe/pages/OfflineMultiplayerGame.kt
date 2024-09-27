package com.shrijal.tictactoe.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.shrijal.tictactoe.composable.checkWinner
import com.shrijal.tictactoe.composable.CurrentPlayerText
import com.shrijal.tictactoe.composable.GameModeTitle
import com.shrijal.tictactoe.composable.GameTitle
import com.shrijal.tictactoe.composable.ReturntoMainMenu
import com.shrijal.tictactoe.composable.Scoreboard
import com.shrijal.tictactoe.dialogs.ShowDialogBox
import com.shrijal.tictactoe.navigation.Screens
import com.shrijal.tictactoe.composable.reset
import com.shrijal.tictactoe.ui.theme.*
import kotlin.random.Random

class OfflineMultiplayerGame : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OfflineMultiplayer(rememberNavController())
        }
    }
}

@Composable
fun OfflineMultiplayer(navController: NavController) {
    var board by remember { mutableStateOf(Array(3) { CharArray(3) { ' ' } }) } // Use CharArray for board
    var currentPlayer by remember { mutableStateOf('X') }
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


    // Show dialog when the game ends with a winner
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
            winner = ""
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

        Spacer(
            modifier = Modifier
                .height(50.dp)
        )

        // Game UI Design
        GameTitle()

        GameModeTitle(text = "Offline Multiplayer")

        Spacer(modifier = Modifier.height(50.dp))
        // Display current Player when X
        when (currentPlayer == 'X' ) {
            true -> CurrentPlayerText(currentPlayer = currentPlayer.toString())
            false -> CurrentPlayerText(currentPlayer = "")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Game Board
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
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(
//                                Image(
//                                    painter = painterResource(id = R.drawable.logo_mark),
//                                    contentDescription = null,
//                                    Modifier
//                                        .size(105.dp, 175.dp)
//                                )
                                color = when (board[i][j]) {
                                    'X' -> SecondaryActivated
                                    'O' -> TertiaryActivated
                                    else -> Deactivated
                                }
                            )
                            .clickable {
                                if (board[i][j] == ' ' && winner.isEmpty()) {
                                    board[i][j] = currentPlayer
                                    currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
                                    winner = checkWinner(board,
                                        onWin = { winningPlayer ->
                                            if (winningPlayer == "X") {
                                                wincountPlayer1++
                                            } else {
                                                wincountPlayer2++
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
        if (currentPlayer == 'O') {
            CurrentPlayerText(currentPlayer = currentPlayer.toString())
        }

        Spacer(modifier = Modifier.height(30.dp))

        //Scoreboard
        Scoreboard(
            wincountplayer1 = wincountPlayer1,
            wincountplayer2 = wincountPlayer2,
            drawCount = drawCount
        )

        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        // End Game Button
        ReturntoMainMenu(navController = navController)
    }
}



@Preview
@Composable
fun OfflineMultiplayerPreview() {
    OfflineMultiplayer(
        rememberNavController()
    )
}