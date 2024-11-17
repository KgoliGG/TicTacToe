package com.shrijal.tictactoe.composable

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

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
        if (snapshot.getValue(String::class.java) == currentPlayer) {
            val flattenedBoard = board.flatMap { row -> row.map { it.toString() } }
            roomRef.child("board").setValue(flattenedBoard).addOnSuccessListener {
                roomRef.child("currentTurn").setValue(if (currentPlayer == "player1") "player2" else "player1")
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
            val boardData = snapshot.child("board").children.map { it.getValue(String::class.java) ?: "" }
            val currentTurn = snapshot.child("currentTurn").getValue(String::class.java) ?: "player1"
            val status = snapshot.child("status").getValue(String::class.java)

            val boardArray = Array(3) { row ->
                CharArray(3) { col -> boardData.getOrElse(row * 3 + col) { "" }.firstOrNull() ?: ' ' }
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

fun declareGameOver(database: DatabaseReference, gameCode: String, winner: String) {
    database.child("rooms").child(gameCode).updateChildren(
        mapOf("status" to "finished", "winner" to winner)
    )
}
