package navigation

import screen.game.data.BallType

sealed class Screen {

    data class GameSuccessScreen(val score: Int) : Screen()
    data class GameFailureScreen(val score: Int) : Screen()
    data class GameScreen(val dataSet: List<BallType>, val title: String) : Screen()
    data object MainScreen : Screen()
}