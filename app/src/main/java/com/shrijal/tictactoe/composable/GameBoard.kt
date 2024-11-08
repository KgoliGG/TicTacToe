package com.shrijal.tictactoe.composable

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.shrijal.tictactoe.firebase.updateLastActivity

fun makeMove(
    database: DatabaseReference,
    gameCode: String,
    board: Array<CharArray>,
    currentPlayer: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val boardData = board.map { it.toList() } // Convert CharArray to List for Firebase storage

    database.child("rooms").child(gameCode).updateChildren(
        mapOf(
            "board" to boardData,
            "currentTurn" to if (currentPlayer == "X") "O" else "X"
        )
    ).addOnSuccessListener {
        updateLastActivity(database, gameCode) // Update timestamp after each move
        onSuccess()
    }.addOnFailureListener {
        onError(it.message ?: "Failed to update game state")
    }
}

fun listenForGameUpdates(
    database: DatabaseReference,
    gameCode: String,
    onBoardUpdate: (Array<CharArray>, String) -> Unit, // Callback with new board and current player
    onGameOver: (String) -> Unit // Callback when game is over
) {
    database.child("rooms").child(gameCode).addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val board = snapshot.child("board").getValue<List<List<String>>>() ?: return
            val currentTurn = snapshot.child("currentTurn").getValue(String::class.java) ?: "X"
            val status = snapshot.child("status").getValue(String::class.java)

            // Convert board back to Array<CharArray>
            val boardArray = Array(3) { row ->
                CharArray(3) { col ->
                    board[row][col].firstOrNull() ?: ' '
                }
            }

            if (status == "finished") {
                onGameOver("Game Over") // Call the callback for game over
            } else {
                onBoardUpdate(boardArray, currentTurn) // Sync UI with new game state
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error
        }
    })
}

fun declareGameOver(
    database: DatabaseReference,
    gameCode: String,
    winner: String // "X", "O" or "Draw"
) {
    database.child("rooms").child(gameCode).updateChildren(
        mapOf(
            "status" to "finished",
            "winner" to winner
        )
    )
}

