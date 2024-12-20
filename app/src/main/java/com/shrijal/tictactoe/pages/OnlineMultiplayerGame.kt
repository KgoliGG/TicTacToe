package com.shrijal.tictactoe.pages

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.*
import com.shrijal.tictactoe.ui.theme.*
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.*
import com.shrijal.tictactoe.composable.*

class OnlineMultiplayerGameActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlineMultiplayerGame(
                rememberNavController(),
                database,
                gameCode = intent.getStringExtra("gameCode") ?: "",
                username = intent.getStringExtra("username") ?: ""
            )
        }
    }
}

@Composable
fun OnlineMultiplayerGame(
    navController: NavController,
    database: DatabaseReference,
    gameCode: String,
    username: String
) {
//    var board by remember { mutableStateOf(Array(3) { CharArray(3) { ' ' } }) }
    val board = remember { mutableStateOf(Array(3) { CharArray(3) { ' ' } }) }

    var currentPlayer by remember { mutableStateOf("player1") }
    var gameStatus by remember { mutableStateOf("active") }
    var errorMessage by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val playerMark = database.child("rooms").child(gameCode).child("marks").child(username)
    val winner = remember { mutableStateOf("") }


//    listenForGameUpdates(
//        database,
//        gameCode,
//        username,
//        onBoardUpdate = { newBoard, newCurrentPlayer ->
//            board = newBoard
//            currentPlayer = newCurrentPlayer
//        },
//        onGameOver = {
//            gameStatus = "finished"
//            showToast = it
//        }
//    )
    LaunchedEffect(Unit) {
        listenForBoardUpdates(
            database = database,
            code = gameCode,
            onBoardUpdate = { updatedBoard ->
                board.value = updatedBoard
            },
            onGameOver = { gameWinner ->
                winner.value = gameWinner
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Primary).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GameTitle()
        GameModeTitle(text = "Online Multiplayer")

        Spacer(modifier = Modifier.height(50.dp))

        // Game Board
        GameBoard(
            board = board.value,
            currentPlayer = currentPlayer,
            username = username,
            playerMark = playerMark.toString(),
            onMoveMade = { row, col ->
                if (gameStatus == "active") {
                    makeMove(
                        database = database,
                        gameCode = gameCode,
                        row = row,
                        col = col,
                        currentPlayer = username,
                        playerMark = playerMark.toString(),
                        onSuccess = {
                            errorMessage = ""
                        },
                        onError = { error ->
                            errorMessage = error
                        }
                    )
                }
            }
        )


        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = if (gameStatus == "finished") {
                "Game Over"
            }
            else {
                "Player Turn: $currentPlayer"
            },
            style = TextStyle(
                fontFamily = montserrat,
                fontSize = 16.sp,
                fontWeight = FontWeight(400),
                color = Color.White)
        )

        Spacer(
            modifier = Modifier
                .height(20.dp)
        )

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Tertiary
            )
        }

        Spacer(
            modifier = Modifier
                .height(20.dp))

        showToast?.let {
            LaunchedEffect(it) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                showToast = null
            }
        }

        ReturntoMainMenu(navController = navController)
    }
}


@Composable
fun GameBoard(
    board: Array<CharArray>,
    currentPlayer: String,
    username: String,
    playerMark: String,
    onMoveMade: (Int, Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                                when (board[i][j]) {
                                    'X' -> SecondaryActivated
                                    'O' -> TertiaryActivated
                                    else -> Deactivated
                                }
                            )
                            .clickable {
                                if (board[i][j] == ' ' && currentPlayer == username) {
                                    onMoveMade(i, j)
                                }
                            },
                        contentAlignment = Alignment.Center
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
    }
}

@Preview
@Composable
fun OnlineMultiplayerGamePreview() {
    OnlineMultiplayerGame(
        navController = rememberNavController(),
        database = FirebaseDatabase.getInstance().reference,
        gameCode = "123456",
        username = "Player1"
    )
}
