package com.shrijal.tictactoe.firebase

import com.google.firebase.database.*

fun createGameCode(
    database: DatabaseReference,
    user1: String,
    code: String,
    onError: (String) -> Unit,
    onSuccess: () -> Unit
) {
    if (code.isEmpty()) {
        onError("Enter a valid code!")
        return
    }

    // Create Log
    println("Attempting to create code: $code")

    database.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val codeExists = snapshot.children.any { it.key == code }
            if (codeExists) {
                println("Code already exists")
                onError("Code already exists!")
            } else {
                // Create a room under the game code and add the first player
                val roomRef = database.child(code)
                roomRef.child("players").child("player1").setValue(user1).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        println("$code is successfully created with user1: $user1")
                        onSuccess()
                    } else {
                        onError(task.exception?.message ?: "Failed to create game room")
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Create Cancellation Log
            println("Firebase operation cancelled: ${error.message}")
            onError("Error creating game code!")
        }
    })
}


fun joinGameCode(
    database: DatabaseReference,
    username: String,
    code: String,
    onError: (String) -> Unit,
    onSuccess: () -> Unit,
    onRoomFull: () -> Unit,
    onReconnectionAllowed: () -> Unit // Callback when a player is allowed to reconnect
) {
    if (code.isEmpty()) {
        onError("Please enter a valid room code!")
        return
    }

    val roomRef = database.child(code)

    roomRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (!snapshot.exists()) {
                onError("Room does not exist!")
            } else {
                // Fetch player1 and player2 details
                val player1 = snapshot.child("players").child("player1").getValue(String::class.java)
                val player2 = snapshot.child("players").child("player2").getValue(String::class.java)

                when {
                    // If both players exist, check for reconnection
                    player1 != null && player2 != null -> {
                        when {
                            // Allow reconnection for player1
                            player1 == username -> {
                                onReconnectionAllowed()
                            }
                            // Allow reconnection for player2
                            player2 == username -> {
                                onReconnectionAllowed()
                            }
                            // Room is full and both players are different, so deny entry
                            else -> {
                                onRoomFull()
                            }
                        }
                    }
                    // If player1 is present but player2 is not, allow player2 to join
                    player1 != null && player2 == null -> {
                        if (player1 == username) {
                            onReconnectionAllowed() // Player1 is reconnecting
                        } else {
                            // Add the second player (player2)
                            roomRef.child("players").child("player2").setValue(username)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        onSuccess()
                                    } else {
                                        onError(task.exception?.message ?: "Failed to join room")
                                    }
                                }
                        }
                    }
                    // If player1 is not present, assign the current user as player1
                    player1 == null -> {
                        roomRef.child("players").child("player1").setValue(username)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    onSuccess()
                                } else {
                                    onError(task.exception?.message ?: "Failed to join room")
                                }
                            }
                    }
                    // In any other case, error out
                    else -> {
                        onError("Room is in an invalid state.")
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onError(error.message)
        }
    })
}
