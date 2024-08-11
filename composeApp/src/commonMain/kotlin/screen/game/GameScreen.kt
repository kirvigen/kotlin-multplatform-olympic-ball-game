package screen.game

import BallGame
import GameState
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ballsgame.composeapp.generated.resources.Res
import ballsgame.composeapp.generated.resources.bg
import ballsgame.composeapp.generated.resources.bottom_part
import ballsgame.composeapp.generated.resources.column
import ballsgame.composeapp.generated.resources.data_set_title
import ballsgame.composeapp.generated.resources.score
import components.OlympicButton
import navigation.Navigator
import navigation.Screen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pxToDp
import screen.game.data.BallType

@Composable
fun GameScreen(dataSet: List<BallType>, title: String) {
    val gameState = remember {
        GameState(dataSet) {
            //TODO("Game ended")
        }
    }
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color.Green,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    var isDataSetShowNeeded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(modifier = Modifier.fillMaxWidth().width(20.dp).background(color.copy(alpha = 0.4f)))
            Box(
                Modifier.height(50.dp).fillMaxWidth().background(Color.White)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    modifier = Modifier.padding(start = 12.dp).align(Alignment.CenterStart)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false)
                        ) {
                            Navigator.navigateTo(Screen.MainScreen)
                        },
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = title,
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(Modifier.padding(top = 16.dp)) {
                Image(
                    modifier = Modifier.padding(start = 10.dp).height(400.dp),
                    contentScale = ContentScale.FillHeight,
                    painter = painterResource(Res.drawable.column),
                    contentDescription = "Column"
                )
                Box(modifier = Modifier.weight(1f).height(400.dp)) {
                    BallGame(gameState)
                }
                Image(
                    modifier = Modifier.padding(end = 10.dp).height(400.dp),
                    contentScale = ContentScale.FillHeight,
                    painter = painterResource(Res.drawable.column),
                    contentDescription = "Column"
                )
            }

            Image(
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(Res.drawable.bottom_part),
                contentDescription = null
            )


        }

        Column(
            Modifier.align(Alignment.BottomCenter)
        ) {

            AnimatedContent(
                modifier = Modifier.padding(bottom = 24.dp).align(Alignment.CenterHorizontally),
                targetState = gameState.score,
                transitionSpec = { scaleIn(tween()).togetherWith(scaleOut(tween())) }
            ) { value ->
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(Res.string.score) + " $value",
                    fontSize = 16.sp
                )
            }
            OlympicButton(
                text = Res.string.data_set_title,
                modifier = Modifier.padding(bottom = 16.dp).padding(horizontal = 24.dp)

            ) {
                isDataSetShowNeeded = true
            }
        }

        if (isDataSetShowNeeded) {
            DataInfoScreen(dataSet) {
                isDataSetShowNeeded = false
            }
        }
    }

}