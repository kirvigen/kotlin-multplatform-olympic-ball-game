package screen.game.data

import com.benasher44.uuid.uuid4


data class Ball(
    var x: Float,
    var y: Float,
    var radius: Float,
    val type: BallType,
    var velocityX: Float = 0f,
    var velocityY: Float = 0f,
    var mass: Float = 1f,
    var wasInPlayZone: Boolean = false,
    val id: String = uuid4().toString(),
)
