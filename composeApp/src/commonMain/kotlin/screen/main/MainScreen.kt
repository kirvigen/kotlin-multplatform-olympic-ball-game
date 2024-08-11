package screen.main

import DataSet
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ballsgame.composeapp.generated.resources.Res
import ballsgame.composeapp.generated.resources.bg
import ballsgame.composeapp.generated.resources.btn_famous_athletes
import ballsgame.composeapp.generated.resources.btn_summer_games
import ballsgame.composeapp.generated.resources.main_screen_title
import ballsgame.composeapp.generated.resources.olympic_logo
import components.OlympicButton
import kotlinx.coroutines.launch
import navigation.Navigator
import navigation.Screen
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen() {
    val coroutine = rememberCoroutineScope()
    Box(Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(Res.drawable.bg),
            contentScale = ContentScale.Crop,
            alpha = 0.5f,
            contentDescription = null
        )

        Column(
            modifier = Modifier.fillMaxWidth().statusBarsPadding().navigationBarsPadding()
                .align(Alignment.Center)
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                lineHeight = 40.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp).padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(Res.string.main_screen_title)
            )

            OlympicButton(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = Res.string.btn_summer_games
            ) {
                coroutine.launch {
                    Navigator.navigateTo(
                        Screen.GameScreen(
                            DataSet.olympicSummerCities,
                            getString(Res.string.btn_summer_games)
                        )
                    )
                }
            }

            OlympicButton(
                modifier = Modifier.padding(horizontal = 24.dp).padding(top = 16.dp),
                text = Res.string.btn_famous_athletes
            ) {
                coroutine.launch {
                    Navigator.navigateTo(
                        Screen.GameScreen(
                            DataSet.famousAthletes,
                            getString(Res.string.btn_famous_athletes)
                        )
                    )
                }
            }
        }
    }

}