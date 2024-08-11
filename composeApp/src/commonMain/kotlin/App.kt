import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import navigation.Navigator
import navigation.Screen
import org.jetbrains.compose.ui.tooling.preview.Preview
import screen.game.GameScreen
import screen.main.MainScreen


@Composable
@Preview
fun App() {
    val activeScreen = remember { Navigator.activeState }
    MaterialTheme(
        colors = MaterialTheme.colors.copy(primary = Color(0, 120, 209))
    ) {
        Box(Modifier.fillMaxWidth().background(Color.White)) {
            AnimatedContent(
                targetState = activeScreen.value,
                transitionSpec = { (fadeIn()).togetherWith(fadeOut()) }
            ) { target ->
                when (target) {
                    is Screen.MainScreen -> {
                        MainScreen()
                    }

                    is Screen.GameScreen -> {
                        GameScreen(target.dataSet, target.title)
                    }

                    is Screen.GameFailureScreen -> {

                    }

                    is Screen.GameSuccessScreen -> {

                    }
                }
            }
        }
    }
}