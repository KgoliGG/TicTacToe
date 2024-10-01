package com.shrijal.tictactoe.pages

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.*
import com.shrijal.tictactoe.composable.CurrentPlayerText
import com.shrijal.tictactoe.composable.ReturntoMainMenu
import com.shrijal.tictactoe.firebase.*
import com.shrijal.tictactoe.ui.theme.*

@Composable
fun OnlineMultiplayerGame(
    navController: NavController,
    gameCode: String,
    username: String,
    database: DatabaseReference
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary)
            .padding(vertical = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = gameCode,
            style = TextStyle(
                fontFamily = montserrat,
                fontWeight = FontWeight(800),
                fontSize = 50.sp,
                color = Color.White,
            )
        )
        Text(
            text = username,
            style = TextStyle(
                fontFamily = montserrat,
                fontWeight = FontWeight(800),
                fontSize = 50.sp,
                color = Color.White,
            )
        )
        Text(
            text = database.toString(),
            style = TextStyle(
                fontFamily = montserrat,
                fontWeight = FontWeight(800),
                fontSize = 50.sp,
                color = Color.White,
            )
        )
    }

}

