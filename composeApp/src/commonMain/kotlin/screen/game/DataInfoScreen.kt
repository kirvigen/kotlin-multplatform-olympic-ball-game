package screen.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import ballsgame.composeapp.generated.resources.data_set_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import screen.game.data.BallType

@Composable
fun DataInfoScreen(dataSet: List<BallType>, onBack: () -> Unit) {
    Box(Modifier.background(Color.White).clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null
    ) {  }) {
        LazyVerticalGrid(
            modifier = Modifier.padding(top = 50.dp),
            columns = GridCells.Adaptive(minSize = 160.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 16.dp, start = 8.dp, end = 8.dp, bottom = 10.dp)
        ) {
            items(dataSet, key = { it.name }) {
                val position = dataSet.size - dataSet.indexOf(it)
                ItemInfo(position, it)
            }
        }
        Column {
            Box(
                Modifier.height(50.dp).fillMaxWidth().background(Color.White)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    modifier = Modifier.align(Alignment.CenterStart).padding(start = 12.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false)
                        ) {
                            onBack.invoke()
                        },
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(Res.string.data_set_title),
                    fontSize = 20.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth().height(16.dp).background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.White,
                            Color.Transparent
                        )
                    )
                )
            )
        }
    }
}

@Composable
fun ItemInfo(position: Int, ballType: BallType) {
    Box(Modifier.fillMaxWidth().height(240.dp).clip(RoundedCornerShape(8.dp))) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(ballType.image),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxHeight(0.7f).fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                ).padding(8.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "$position) " + ballType.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
                text = ballType.additionalInformation,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}