package components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OlympicButton(modifier: Modifier = Modifier, text: StringResource, onClick: ()-> Unit) {
    Box(
        modifier = Modifier.then(modifier).shadow(
            10.dp,
            shape = RoundedCornerShape(12.dp),
            ambientColor = Color.Transparent,
            spotColor = Color.Black.copy(alpha = 0.5f)
        ).fillMaxWidth().height(50.dp).background(
            color = MaterialTheme.colors.primary,
            shape = RoundedCornerShape(12.dp)
        ).clickable {
            onClick.invoke()
        }
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(text),
            color = Color.White
        )
    }
}