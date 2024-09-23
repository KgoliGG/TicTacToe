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
    onSuccess: () -> Unit
) {
    if (code.isEmpty()) {
        onError("Enter a valid code!")
        return
    }

    //Join Log
    println("Attempting to join code: $code")

    database.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val codeExists = snapshot.children.any { it.value == code }
            if (codeExists) {
                //Join Validation Log
                println("Game Room available")
                onSuccess()
            } else {
                //Join Validation Log
                println("Invalid game code!")
                onError("Invalid game code!")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            //Join Cancellation Log
            println("Error joining game!")
            onError("Error joining game!")
        }
    })
}
