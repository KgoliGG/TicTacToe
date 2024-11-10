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

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val codeExists = snapshot.hasChild(code)
                if (codeExists) {
                    onError("Code already exists!")
                } else {
                    // Create a new game room under the game code
                    val roomRef = database.child(code)
                    roomRef.child("players").child("player1").setValue(username).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Initialize game board and settings
                            roomRef.child("board").setValue(List(9) { "" })  // Empty 3x3 board
                            roomRef.child("currentTurn").setValue("player1")
                            roomRef.child("winner").setValue("")
                            roomRef.child("playerMarks").setValue(mapOf("player1" to "X", "player2" to "O"))  // Assign marks
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
        })

        val expiryTime = 30 * 60 * 1000 // 30 minutes in milliseconds
        database.child("rooms").child(code).onDisconnect().removeValue() // Cleanup if the client disconnects
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
        val roomRef = database.child(code)
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
                                        roomRef.child("playerMarks").setValue(mapOf("player1" to "X", "player2" to "O")) // Assign marks
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
