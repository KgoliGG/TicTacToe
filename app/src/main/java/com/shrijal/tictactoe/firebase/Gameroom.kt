package com.shrijal.tictactoe.firebase

import com.google.firebase.database.*

fun createGameCode(
    database: DatabaseReference,
    code: String,
    onError: (String) -> Unit,
    onSuccess: () -> Unit
) {
    if (code.isEmpty()) {
        onError("Enter a valid code!")
        return
    }

    //Create Log
    println("Attempting to create code: $code")

    database.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val codeExists = snapshot.children.any { it.value == code }
            if (codeExists) {
                println("Code already exists")
                onError("Code already exists!")
            } else {
                database.push().setValue(code).addOnCompleteListener {
                    task ->
                    if (task.isSuccessful) {
                        //Create Validation Log
                        println("$code is successfully created in database")
                        onSuccess()
                    }
                    else{
                        //Create Validation Log
                        println("Failed to create the game code: $code")
                        onError("Failed to create the game code: $code")
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            //Create Cancellation Log
            println("Firebase operation cancelled: ${error.message}")
            onError("Error creating game code!")
        }
    })
}

fun joinGameCode(
    database: DatabaseReference,
    code: String,
    onError: (String) -> Unit,
    onSuccess: () -> Unit,
    onRoomFull: () -> Unit
) {
    if (code.isEmpty()) {
        onError("Please enter a valid room code!")
        return
    }

    val roomRef = database.child(code)
    roomRef.child("players").addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val playerCount = snapshot.getValue(Int::class.java) ?: 0
            if (playerCount >= 2) {
                onRoomFull()
            } else {
                roomRef.child("players").setValue(playerCount + 1).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onError(task.exception?.message ?: "Failed to join room")
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onError(error.message)
        }
    })
}