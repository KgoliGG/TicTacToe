package com.shrijal.tictactoe.firebase

import com.google.firebase.database.FirebaseDatabase


data class GameData(
    val board: List<String> = listOf("", "", "", "", "", "", "", "", ""),
    val currentTurn: String = "player1",
    val player1: String = "",
    val player2: String = "",
    val status: String = "ongoing"
)