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
    else if (user1.isEmpty()){
        onError("Username is required")
        return
    }
    else{
        // Create Log
        println("Attempting to create code: $code")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val codeExists = snapshot.children.any { it.key == code }
                if (codeExists) {
//                    println("Code alreadyy exists")
                    onError("Code already exists!")
                } else {
                    // Create a room under the game code and add the first player
                    val roomRef = database.child(code)
                    roomRef.child("gamecodes").child("Player 1").setValue(user1).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
//                            println("$code is successfully created with user1: $user1")
                            roomRef.child("board").setValue(listOf("", "", "", "", "", "", "", "", ""))
                            roomRef.child("currentTurn").setValue("player1")
                            roomRef.child("winner").setValue("")
                            onSuccess()
                        } else {
                            onError(task.exception?.message ?: "Failed to create game room")
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Create Cancellation Log
//                println("Firebase operation cancelled: ${error.message}")
                onError("Error creating game code!")
            }
        }
        )
    }
    val expiryTime = 30 * 60 * 1000 // 30 minutes in milliseconds
    database.child("gamecodes").child(code).onDisconnect().removeValue() // Cleanup if the client disconnects
    scheduleRoomExpiry(database, code, expiryTime)
}



fun joinGameCode(
    database: DatabaseReference,
    username: String,
    code: String,
    onError: (String) -> Unit,
    onSuccess: (GameData) -> Unit, // Pass back the game data
    onRoomFull: () -> Unit,
    onReconnectionAllowed: () -> Unit // Callback for reconnection
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
                                    val gameData = GameData(
                                        board = listOf("", "", "", "", "", "", "", "", ""),
                                        currentTurn = "player1",
                                        player1 = username,
                                        player2 = null.toString()
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

