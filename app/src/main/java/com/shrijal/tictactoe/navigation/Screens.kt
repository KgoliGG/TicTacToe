package com.shrijal.tictactoe.navigation

enum class Screens(val route: String){
        LandingPage("landingpage"),
        MachineLearningModel("machinelearningmodel"),
        OfflineMultiplayerGame("offlinemultiplayergame"),
        OnlineMultiplayerGame("onlineMultiplayerGame/{username}/{gameCode}"),
        GameRoomManagement("gameroommanagement")
}