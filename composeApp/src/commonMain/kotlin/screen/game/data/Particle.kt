package screen.game.data

data class Particle(
    var x: Float,
    var y: Float,
    var radius: Float,
    val type: BallType,
    var velocityX: Float,
    var velocityY: Float,
    var lifespan: Float
)