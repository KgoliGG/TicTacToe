package com.shrijal.tictactoe.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

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

    database.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val codeExists = snapshot.children.any { it.value == code }
            if (codeExists) {
                onError("Code already exists!")
            } else {
                database.push().setValue(code)
                onSuccess()
            }
        }

        override fun onCancelled(error: DatabaseError) {
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

    database.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val codeExists = snapshot.children.any { it.value == code }
            if (codeExists) {
                onSuccess()
            } else {
                onError("Invalid game code!")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onError("Error joining game!")
        }
    })
}
