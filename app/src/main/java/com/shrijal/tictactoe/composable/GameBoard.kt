package com.shrijal.tictactoe.composable

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.shrijal.tictactoe.firebase.updateLastActivity

fun setupGame(database: DatabaseReference, gameCode: String, username: String) {
    val roomRef = database.child("rooms").child(gameCode)

    roomRef.child("players").get().addOnSuccessListener { snapshot ->
        if (!snapshot.hasChild("player1")) {
            // Assign player1 as this user
            roomRef.child("players/player1").setValue(username)
            roomRef.child("marks/player1").setValue("X") // player1 gets "X"
            roomRef.child("currentTurn").setValue("player1") // Set player1's turn
            roomRef.child("board").setValue(List(9) { "" }) // Initialize empty board for player1
        } else if (!snapshot.hasChild("player2")) {
            // Assign player2 as this user
            roomRef.child("players/player2").setValue(username)
            roomRef.child("marks/player2").setValue("O") // player2 gets "O"
        }
    }
}

fun makeMove(
    database: DatabaseReference,
    gameCode: String,
    board: Array<CharArray>,
    currentPlayer: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val roomRef = database.child("rooms").child(gameCode)

    roomRef.child("currentTurn").get().addOnSuccessListener { snapshot ->
        val turn = snapshot.getValue(String::class.java)

        // Allow the move only if it's the player's turn
        if (turn == currentPlayer) {
            // Flatten the board manually into a list of strings
            val flattenedBoard = board.flatMap { row -> row.map { it.toString() } }

            roomRef.child("board").setValue(flattenedBoard).addOnSuccessListener {
                // Switch turns
                val nextPlayer = if (currentPlayer == "player1") "player2" else "player1"
                roomRef.child("currentTurn").setValue(nextPlayer)
                onSuccess()
            }.addOnFailureListener {
                onError("Failed to make move")
            }
        } else {
            onError("Not your turn!")
        }
    }
}

fun listenForGameUpdates(
    database: DatabaseReference,
    gameCode: String,
    username: String,
    onBoardUpdate: (Array<CharArray>, String) -> Unit,
    onGameOver: (String) -> Unit
) {
    val roomRef = database.child("rooms").child(gameCode)

    roomRef.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            // Ensure that board data is properly initialized
            val boardData = snapshot.child("board").children.map { it.getValue(String::class.java) ?: "" }
            val currentTurn = snapshot.child("currentTurn").getValue(String::class.java) ?: "player1"
            val status = snapshot.child("status").getValue(String::class.java)

            // Handle the case where the board is empty or not yet set
            val boardArray = if (boardData.isNotEmpty()) {
                Array(3) { row ->
                    CharArray(3) { col ->
                        boardData.getOrElse(row * 3 + col) { "" }.firstOrNull() ?: ' '
                    }
                }
            } else {
                Array(3) { CharArray(3) { ' ' } } // Default empty board
            }

            if (status == "finished") {
                val winner = snapshot.child("winner").getValue(String::class.java)
                onGameOver("Game Over! Winner: $winner")
            } else {
                onBoardUpdate(boardArray, currentTurn)
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
