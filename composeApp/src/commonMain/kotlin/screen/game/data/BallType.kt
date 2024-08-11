package screen.game.data

import org.jetbrains.compose.resources.DrawableResource

data class BallType(
    val image: DrawableResource,
    val mass: Int,
    val name: String,
    val additionalInformation: String
)