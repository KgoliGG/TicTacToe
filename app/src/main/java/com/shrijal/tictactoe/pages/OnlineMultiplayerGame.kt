package com.shrijal.tictactoe.pages

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
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
import com.shrijal.tictactoe.composable.GameModeTitle
import com.shrijal.tictactoe.composable.GameTitle
import com.shrijal.tictactoe.composable.ReturntoMainMenu
import com.shrijal.tictactoe.composable.Scoreboard
import com.shrijal.tictactoe.composable.reset
import com.shrijal.tictactoe.dialogs.ShowDialogBox
import com.shrijal.tictactoe.firebase.*
import com.shrijal.tictactoe.navigation.Screens
import com.shrijal.tictactoe.ui.theme.*
import kotlin.random.Random

@Composable
fun OnlineMultiplayerGame(
    navController: NavController,
    gameCode: String,
    currentPlayer: String,
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

        Spacer(
            modifier = Modifier
                .height(50.dp)
        )

        // Game UI Design
        GameTitle()

        GameModeTitle(text = "Online Multiplayer")

        Spacer(
            modifier = Modifier
                .height(50.dp)
        )

        //Game Board
        for (i in 0 .. 2) {
            Row {
                for (j in 0 ..2) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
                            .background(
                                color = Deactivated
                            )
                        ,
                        contentAlignment = Alignment.Center
                    ) {


                    }
                }
            }
        }



        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        // End Game Button
        ReturntoMainMenu(navController = navController)

    }
}