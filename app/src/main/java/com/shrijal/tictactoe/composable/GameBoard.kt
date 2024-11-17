package com.shrijal.tictactoe.composable

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

fun makeMove(
    database: DatabaseReference,
    gameCode: String,
    row: Int,
    col: Int,
    currentPlayer: String,
    playerMark: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val roomRef = database.child("rooms").child(gameCode)

    // Fetch the current turn and board from the database
    roomRef.child("currentTurn").get().addOnSuccessListener { turnSnapshot ->
        val currentTurn = turnSnapshot.getValue(String::class.java)
        if (currentTurn == currentPlayer) {
            roomRef.child("board").get().addOnSuccessListener { boardSnapshot ->
                val board = boardSnapshot.children.map { it.getValue(String::class.java) ?: "" }.toMutableList()
                val index = row * 3 + col

                // Check if the selected box is empty
                if (board[index].isEmpty()) {
                    board[index] = playerMark // Mark the box
                    roomRef.child("board").setValue(board).addOnSuccessListener {
                        // Switch the turn to the other player
                        val nextTurn = if (currentPlayer == "player1") "player2" else "player1"
                        roomRef.child("currentTurn").setValue(nextTurn).addOnSuccessListener {
                            onSuccess()
                        }.addOnFailureListener { e ->
                            onError("Failed to update turn: ${e.message}")
                        }
                    }.addOnFailureListener { e ->
                        onError("Failed to update board: ${e.message}")
                    }
                } else {
                    onError("Cell already occupied!")
                }
            }.addOnFailureListener { e ->
                onError("Failed to fetch board: ${e.message}")
            }
        } else {
            onError("Not your turn!")
        }
    }.addOnFailureListener { e ->
        onError("Failed to fetch current turn: ${e.message}")
    }
}



//fun listenForGameUpdates(
//    database: DatabaseReference,
//    gameCode: String,
//    username: String,
//    onBoardUpdate: (Array<CharArray>, String) -> Unit,
//    onGameOver: (String) -> Unit
//) {
//    val roomRef = database.child("rooms").child(gameCode)
//
//    roomRef.addValueEventListener(object : ValueEventListener {
//        override fun onDataChange(snapshot: DataSnapshot) {
//            val boardData = snapshot.child("board").children.map { it.getValue(String::class.java) ?: "" }
//            val currentTurn = snapshot.child("currentTurn").getValue(String::class.java) ?: "player1"
//            val status = snapshot.child("status").getValue(String::class.java)
//
//            val boardArray = Array(3) { row ->
//                CharArray(3) { col -> boardData.getOrElse(row * 3 + col) { "" }.firstOrNull() ?: ' ' }
//            }
//
//            if (status == "finished") {
//                val winner = snapshot.child("winner").getValue(String::class.java)
//                onGameOver("Game Over! Winner: $winner")
//            } else {
//                onBoardUpdate(boardArray, currentTurn)
//            }
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//            // Handle error
//        }
//    })
//}

fun updateMove(
    database: DatabaseReference,
    code: String,
    row: Int,
    col: Int,
    currentPlayer: String,
    onError: (String) -> Unit
) {
    val roomRef = database.child("rooms").child(code)

    roomRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val board = snapshot.child("board").getValue<ArrayList<ArrayList<String>>>()
            val currentTurn = snapshot.child("currentTurn").value as? String

            if (board == null || currentTurn == null) {
                onError("Invalid game state!")
                return
            }

            // Check if the cell is empty
            if (board[row][col] != " ") {
                onError("Cell already occupied!")
                return
            }

            if (currentTurn != currentPlayer) {
                onError("It's not your turn!")
                return
            }

            // Update the board with the current player's mark
            board[row][col] = if (currentPlayer == "player1") "X" else "O"

            roomRef.child("board").setValue(board).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Switch turn to the next player
                    roomRef.child("currentTurn").setValue(
                        if (currentPlayer == "player1") "player2" else "player1"
                    )
                } else {
                    onError(task.exception?.message ?: "Failed to update move")
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onError("Error accessing the database: ${error.message}")
        }
    })
}




fun listenForBoardUpdates(
    database: DatabaseReference,
    code: String,
    onBoardUpdate: (Array<CharArray>) -> Unit,
    onGameOver: (String) -> Unit
) {
    val roomRef = database.child("rooms").child(code)

    roomRef.addChildEventListener(object : ChildEventListener {
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            when (snapshot.key) {
                "board" -> {
                    val updatedBoard = snapshot.getValue<ArrayList<ArrayList<String>>>()
                    if (updatedBoard != null) {
                        // Convert to Array<CharArray>
                        val charBoard = Array(3) { CharArray(3) { ' ' } }
                        for (i in 0 until 3) {
                            for (j in 0 until 3) {
                                charBoard[i][j] = updatedBoard[i][j].first() // Convert to Char
                            }
                        }
                        onBoardUpdate(charBoard)
                    }
                }
                "winner" -> {
                    val winner = snapshot.getValue<String>()
                    if (!winner.isNullOrEmpty()) {
                        onGameOver(winner)
                    }
                }
            }
        }

        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {
            println("Error listening for updates: ${error.message}")
        }
    })
}



fun declareGameOver(database: DatabaseReference, gameCode: String, winner: String) {
    database.child("rooms").child(gameCode).updateChildren(
        mapOf("status" to "finished", "winner" to winner)
    )
}
