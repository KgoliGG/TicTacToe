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
import com.shrijal.tictactoe.composable.ReturntoMainMenu
import com.shrijal.tictactoe.composable.makeMove
import com.shrijal.tictactoe.ui.theme.*
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import com.shrijal.tictactoe.composable.GameModeTitle
import com.shrijal.tictactoe.composable.GameTitle

// Firebase reference to room data

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
    var board by remember { mutableStateOf(Array(3) { CharArray(3) { ' ' } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var gameStatus by remember { mutableStateOf("active") }
    var errorMessage by remember { mutableStateOf("") }
    var showToast by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    // Listen to game state changes from Firebase
    listenForGameUpdates(
        database,
        gameCode,
        onBoardUpdate = { newBoard, newCurrentPlayer ->
            board = newBoard
            currentPlayer = newCurrentPlayer
        },
        onGameOver = {
            gameStatus = "finished"
            showToast = it
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Game UI Design
        GameTitle()

        GameModeTitle(text = "Offline Multiplayer")

        Spacer(
            modifier = Modifier
                .height(50.dp)
        )

        // Display the Tic-Tac-Toe board
        GameBoard(board = board, onMoveMade = { row, col ->
            if (gameStatus == "active" && board[row][col] == ' ' && currentPlayer == username.first().toString()) {
                board[row][col] = currentPlayer.first() // Update board locally
                makeMove(database, gameCode, board, currentPlayer, onSuccess = {
                    errorMessage = ""
                }, onError = {
                    errorMessage = it
                })
            }
        })

        Spacer(modifier = Modifier.height(20.dp))

        // Display the current player turn
        Text(
            text = if (gameStatus == "finished") "Game Over" else "Player Turn: $currentPlayer",
            style = TextStyle(
                fontFamily = montserrat,
                fontSize = 16.sp,
                fontWeight = FontWeight(400),
                color = Color.White
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Tertiary)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Show Toast for game over or notifications
        showToast?.let {
            LaunchedEffect(it) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                showToast = null
            }
        }

        // Return to Main Menu Button
        ReturntoMainMenu(navController = navController)
    }
}

@Composable
fun GameBoard(
    board: Array<CharArray>,
    onMoveMade: (Int, Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                                color = when (board[i][j]) {
                                    'X' -> SecondaryActivated
                                    'O' -> TertiaryActivated
                                    else -> Deactivated
                                }
                            )
                            .clickable { if (board[i][j] == ' ') onMoveMade(i, j) },
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

        Spacer(modifier = Modifier.height(20.dp))

    }
}

// Utility function to listen for game updates
fun listenForGameUpdates(
    database: DatabaseReference,
    gameCode: String,
    onBoardUpdate: (Array<CharArray>, String) -> Unit,
    onGameOver: (String) -> Unit
) {
    database.child("rooms").child(gameCode).addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val boardData = snapshot.child("board").getValue<List<List<String>>>() ?: return
            val currentTurn = snapshot.child("currentTurn").getValue(String::class.java) ?: "X"
            val status = snapshot.child("status").getValue(String::class.java)

            val boardArray = Array(3) { row ->
                CharArray(3) { col ->
                    boardData[row][col].firstOrNull() ?: ' '
                }
            }

            if (status == "finished") {
                onGameOver("Game Over!")
            } else {
                onBoardUpdate(boardArray, currentTurn)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error
        }
    })
}

@Preview
@Composable
fun OnlineMultiplayerGamePreview() {
    OnlineMultiplayerGame(
        navController = rememberNavController(),
        database = database,
        gameCode = "123456",
        username = "Player1"
    )
}
