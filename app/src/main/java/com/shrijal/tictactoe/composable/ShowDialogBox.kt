package com.shrijal.tictactoe.dialogs

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.shrijal.tictactoe.ui.theme.*

@Composable
fun ShowDialogBox(
    showDialog: Boolean,
    dialogMessage: String,
    onDismiss: () -> Unit,
    onPlayAgain: () -> Unit,
    onExit: () -> Unit,
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Game Over"
                )
            },
            text = {
                Text(
                    text = dialogMessage
                )
            },
            confirmButton = {
                Button(
                    onClick = onPlayAgain,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Secondary
                    ),
                ) {
                    Text(
                        text = "Play Again",
                        style = TextStyle(
                            fontFamily = montserrat,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(600),
                            color = Primary
                        ),
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = onExit,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Tertiary
                    ),
                ) {
                    Text(
                        text = "Exit",
                        style = TextStyle(
                            fontFamily = montserrat,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(600),
                            color = Primary
                        ),
                    )
                }
            }
        )
    }
}
