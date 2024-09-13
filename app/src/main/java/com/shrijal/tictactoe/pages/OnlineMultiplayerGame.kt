package com.shrijal.tictactoe.pages

import android.content.ContentValues.TAG
import android.util.Log
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
//import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

val database = Firebase.database
val myRef = database.getReference("message")

//myRef.setValue("Hello, World!")
//
//myRef.addValueEventListener(object: ValueEventListener {
//
//    override fun onDataChange(snapshot: DataSnapshot) {
//        // This method is called once with the initial value and again
//        // whenever data at this location is updated.
//        val value = snapshot.getValue<String>()
//        Log.d(TAG, "Value is: " + value)
//    }
//
//    override fun onCancelled(error: DatabaseError) {
//        Log.w(TAG, "Failed to read value.", error.toException())
//    }
//
//}


//@Composable
//fun OnlineMultiplayerGame(navController: NavController){
//    val database = FirebaseDatabase.getInstance().reference
//
//    fun createGame(player1Uid: String) {
//        val gameId = database.child("games").push().key!!
//        val gameData = mapOf(
//            "player1" to player1Uid,
//            "player2" to "",
//            "board" to List(9) { 0 },  // Empty board
//            "turn" to "player1",
//            "status" to "waiting_for_player2"
//        )
//        database.child("games").child(gameId).setValue(gameData)
//    }
//}