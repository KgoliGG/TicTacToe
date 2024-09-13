package com.shrijal.tictactoe.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.shrijal.tictactoe.navigation.Screens
import com.shrijal.tictactoe.ui.theme.*

@Composable
fun ReturntoMainMenu(navController: NavController){
    Button(
        onClick = {
            navController.navigate(
                route = Screens.LandingPage.name
            )
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Primary
        ),
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .clip(
                shape = RoundedCornerShape(100.dp)
            )
            .border(
                1.dp,
                color = Tertiary,
                shape = RoundedCornerShape(100.dp)
            ),
    ) {
        Text(
            text = "X",
            style = TextStyle(
                fontFamily = montserrat,
                fontSize = 28.sp,
                fontWeight = FontWeight(400),
                color = Color.White
            ),
            textAlign = TextAlign.Center
        )
    }
}