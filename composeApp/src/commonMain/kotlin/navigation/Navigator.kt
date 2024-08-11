package navigation

import androidx.compose.runtime.mutableStateOf

object Navigator {

    var activeState = mutableStateOf<Screen>(Screen.MainScreen)
        private set


    fun navigateTo(screen: Screen) {
        activeState.value = screen
    }
}