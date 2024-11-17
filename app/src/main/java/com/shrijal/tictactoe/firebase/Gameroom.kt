package com.shrijal.tictactoe.firebase

import com.google.firebase.database.*

fun createGameCode(
    database: DatabaseReference,
    username: String,
    code: String,
    onError: (String) -> Unit,
    onSuccess: () -> Unit
) {
    if (code.isEmpty()) {
        onError("Enter a valid code!")
        return
    } else if (username.isEmpty()) {
        onError("Username is required")
        return
    } else {
        println("Attempting to create code: $code")

        // Check if the room code already exists
        database.child("rooms").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val codeExists = snapshot.hasChild(code) // Check if room exists
                if (codeExists) {
                    onError("Room code already exists. Try a different one!")
                } else {
                    val roomRef = database.child("rooms").child(code)
                    roomRef.child("players").child("player1").setValue(username)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Initialize room data
                                roomRef.child("board").setValue(List(9) { "" }) // Empty board
                                roomRef.child("marks").child(username)
                                    .setValue("X") // Assign 'X' to player1
                                roomRef.child("winner").setValue("") // No winner initially
                                roomRef.child("currentTurn").setValue("player1") // Set first turn
                                roomRef.child("status").setValue("active") // Mark room as active
                                onSuccess()
                            } else {
                                onError(task.exception?.message ?: "Failed to create game room")
                            }
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
//                Create Cancellation Log
//                println("Firebase operation cancelled: ${error.message}")
                onError("Error creating game code!")
            }
        }
        )

        val expiryTime = 30 * 60 * 1000 // 30 minutes in milliseconds
        database.child("players").child(code).onDisconnect()
            .removeValue() // Cleanup if the client disconnects
        scheduleRoomExpiry(database, code, expiryTime)
    }
}


fun joinGameCode(
    database: DatabaseReference,
    username: String,
    code: String,
    onError: (String) -> Unit,
    onSuccess: (GameData) -> Unit,
    onRoomFull: () -> Unit,
    onReconnectionAllowed: () -> Unit
) {
    if (code.isEmpty()) {
        onError("Please enter a valid room code!")
        return
    } else if (username.isEmpty()) {
        onError("Username is required")
        return
    } else {
        val roomRef = database.child("rooms").child(code)
        roomRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    onError("Room does not exist!")
                } else {
                    val player1 = snapshot.child("players").child("player1").getValue(String::class.java)
                    val player2 = snapshot.child("players").child("player2").getValue(String::class.java)

                    when {
                        player1 != null && player2 != null -> {
                            if (player1 == username || player2 == username) {
                                onReconnectionAllowed()
                            } else {
                                onRoomFull()
                            }
                        }
                        player1 != null && player2 == null -> {
                            if (player1 == username) {
                                onReconnectionAllowed()
                            } else {
                                roomRef.child("players").child("player2").setValue(username).addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        roomRef.child("players/player2").setValue(username)
                                        roomRef.child("marks/$username").setValue("O")
                                        val gameData = GameData(
                                            board = snapshot.child("board").children.map { it.getValue(String::class.java) ?: "" },
                                            currentTurn = snapshot.child("currentTurn").getValue(String::class.java) ?: "player1",
                                            player1 = player1,
                                            player2 = username
                                        )
                                        onSuccess(gameData)
                                    } else {
                                        onError(task.exception?.message ?: "Failed to join room")
                                    }
                                }
                            }
                        }
                        player1 == null -> {
                            roomRef.child("players").child("player1").setValue(username).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    roomRef.child("playerMarks").setValue(mapOf("player1" to "X")) // Set player1 mark only
                                    val gameData = GameData(
                                        board = List(9) { "" },
                                        currentTurn = "player1",
                                        player1 = username,
                                        player2 = ""
                                    )
                                    onSuccess(gameData)
                                } else {
                                    onError(task.exception?.message ?: "Failed to join room")
                                }
                            }
                        }
                        else -> onError("Room is in an invalid state.")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }
        })
    }
}



fun scheduleRoomExpiry(database: DatabaseReference, gameCode: String, expiryTime: Int) {
    val currentTime = System.currentTimeMillis()
    val roomRef = database.child("rooms").child(gameCode)

    roomRef.child("lastActivity").addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val lastActivity = snapshot.getValue(Long::class.java) ?: currentTime
            val timeElapsed = currentTime - lastActivity

            if (timeElapsed >= expiryTime) {
                roomRef.removeValue() // Remove the room if it's expired
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error
        }
    })
}

fun updateLastActivity(database: DatabaseReference, gameCode: String) {
    val currentTime = mapOf("timestamp" to ServerValue.TIMESTAMP)
    database.child("rooms").child(gameCode).child("lastActivity").setValue(currentTime)
}
