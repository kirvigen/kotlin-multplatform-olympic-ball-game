import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ballsgame.composeapp.generated.resources.Res
import ballsgame.composeapp.generated.resources.touch_zone
import screen.game.data.Ball
import screen.game.data.BallType
import screen.game.data.Particle
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

private const val MIN_RADIUS = 90f

data class TouchInfo(val position: Offset, val ballType: BallType)

class GameState(private val dataSet: List<BallType>, onGameEndedCallback: () -> Unit) {
    var maxUnlockedMass by mutableStateOf(dataSet.last().mass)
        private set
    var ballTypesOnScreen by mutableStateOf(listOf(dataSet.last()))
        private set

    var currentBallType by mutableStateOf(ballTypesOnScreen.first())
        private set

    var score by mutableStateOf(0)

    init {
        // Сортируем dataSet по возрастанию массы
        dataSet.sortedBy { it.mass }
    }

    fun unlockNewBallType(mass: Int) {
        if (mass > maxUnlockedMass) {
            maxUnlockedMass = mass
            ballTypesOnScreen = dataSet.filter { it.mass <= mass }
            println("Unlocked new ball type for mass $mass")
        }
    }

    fun getBallTypeForMass(mass: Int): BallType {
        return dataSet.findLast { it.mass == mass } ?: dataSet.first()
    }

    fun getRadiusWithMass(mass: Int): Float {
        return min(200f, sqrt((MIN_RADIUS * MIN_RADIUS) + mass * MIN_RADIUS))
    }

    fun updateCurrentBallType() {
        val sizeVisibleBallTypes = ballTypesOnScreen.size
        currentBallType = when {
            sizeVisibleBallTypes == 0 -> return
            sizeVisibleBallTypes == 1 -> ballTypesOnScreen.first()
            else -> {
                ballTypesOnScreen.takeLast(sizeVisibleBallTypes - (sizeVisibleBallTypes / 2))
                    .shuffled().first()
            }
        }
    }

    fun onGameEnded() {

    }
}

@Composable
fun BallGame(config: GameState) {
    val gameState = remember { config }
    var balls by remember { mutableStateOf(listOf<Ball>()) }
    var particles by remember { mutableStateOf(listOf<Particle>()) }
    var currentTouch by remember { mutableStateOf<TouchInfo?>(null) }
    var hasCalledEscapeCallback by remember { mutableStateOf(false) }
    val textMeasurer = rememberTextMeasurer()
    val coroutineScope = rememberCoroutineScope()
    var boxWidth by remember { mutableStateOf(0f) }
    var boxHeight by remember { mutableStateOf(0f) }
    var creationZoneHeight by remember { mutableStateOf(0f) }
    var escapeThreshold by remember { mutableStateOf(0f) }
    val painters = remember { mutableStateMapOf<BallType, Painter>() }
    painters.putAll(gameState.ballTypesOnScreen.associateWith { painterResource(it.image) })

    LaunchedEffect(currentTouch) {
        if (currentTouch == null) {
            gameState.updateCurrentBallType()
        }
    }

    LaunchedEffect(boxWidth, boxHeight) {
        creationZoneHeight = boxHeight / 3f
        escapeThreshold = boxHeight * 0.1f
        println("Screen size updated: width=$boxWidth, height=$boxHeight")
        println("Creation zone height: $creationZoneHeight")
        println("Escape threshold: $escapeThreshold")
    }


    LaunchedEffect(boxWidth, boxHeight) {
        creationZoneHeight = boxHeight / 3f
        escapeThreshold = boxHeight * 0.1f
        println("Screen size updated: width=$boxWidth, height=$boxHeight")
        println("Creation zone height: $creationZoneHeight")
        println("Escape threshold: $escapeThreshold")
    }


    val gravity = 9.8f
    val bounceFactor = 0.8f
    val friction = 0.99f

    DisposableEffect(Unit) {
        val job = coroutineScope.launch {
            while (isActive) {
                try {
                    val scaleFactor = calculateScaleFactor(balls.size)

                    val localBalls = balls.mapNotNull { ball ->
                        try {
                            ball.apply {
                                val oldY = y
                                update(
                                    boxWidth,
                                    boxHeight,
                                    gravity,
                                    friction,
                                    bounceFactor,
                                    scaleFactor
                                )
                                if (oldY > creationZoneHeight && y <= creationZoneHeight) {
                                    wasInPlayZone = true
                                    println("Ball ${ball.id} entered play zone")
                                }

                                val topEdge = y - radius * scaleFactor
                                if (wasInPlayZone && topEdge <= escapeThreshold && y < oldY && velocityY < 0) {
                                    println("Checking escape: Ball ${ball.id} topEdge at $topEdge, threshold at $escapeThreshold")
                                    if (!hasCalledEscapeCallback) {
                                        hasCalledEscapeCallback = true
                                        println("Ball ${ball.id} escaped! TopEdge: $topEdge, Threshold: $escapeThreshold")
                                        gameState.onGameEnded()
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            println("Error updating ball ${ball.id}: ${e.message}")
                            println("Ball state: x=${ball.x}, y=${ball.y}, radius=${ball.radius}, scaledRadius=${ball.radius * scaleFactor}")
                            println("Box dimensions: width=$boxWidth, height=$boxHeight")
                            if (!hasCalledEscapeCallback) {
                                hasCalledEscapeCallback = true
                                gameState.onGameEnded()
                            }
                            null  // Удаляем проблемный шар
                        }
                    }.toMutableList()

                    val localParticle = particles.toMutableList()
                    handleCollisions(localBalls, localParticle, gameState)

                    particles = localParticle
                    balls = localBalls


                    particles = particles.mapNotNull { particle ->
                        particle.update()
                        if (particle.lifespan > 0) particle else null
                    }

                    delay(16)
                } catch (e: CancellationException) {
                    throw e // Позволяем корутине быть отмененной
                } catch (e: Exception) {
                    println("Error in game loop: ${e.message}")
                }
            }
        }
        onDispose {
            job.cancel()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxWidth().height(creationZoneHeight.toInt().pxToDp())
        ) {
            Row {
                Spacer(
                    modifier = Modifier.width(20.dp).fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    Color.Gray.copy(alpha = 0.3f)
                                )
                            )
                        )
                )
                Spacer(
                    modifier = Modifier.weight(1f).fillMaxWidth().fillMaxHeight()
                        .background(color = Color.Gray.copy(alpha = 0.3f))
                )
                Spacer(
                    modifier = Modifier.width(20.dp).fillMaxHeight()
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color.Gray.copy(alpha = 0.3f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }

            Text(
                modifier = Modifier.align(Alignment.Center),
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                text = stringResource(Res.string.touch_zone)
            )
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            when {
                                event.changes.any { it.pressed } -> {
                                    val position = event.changes.first().position
                                    if (position.y <= creationZoneHeight) {
                                        currentTouch =
                                            TouchInfo(position, gameState.currentBallType)
                                    }
                                }

                                event.changes.any { !it.pressed } -> {
                                    currentTouch?.let { touchInfo ->
                                        if (touchInfo.position.y <= creationZoneHeight) {
                                            balls = balls + Ball(
                                                x = touchInfo.position.x,
                                                y = touchInfo.position.y,
                                                radius = gameState.getRadiusWithMass(touchInfo.ballType.mass),
                                                mass = touchInfo.ballType.mass.toFloat(),
                                                type = touchInfo.ballType,
                                                velocityX = 0f,
                                                velocityY = 5f
                                            )
                                            gameState.score += touchInfo.ballType.mass
                                        }
                                    }
                                    currentTouch = null
                                }
                            }
                        }
                    }
                }
                .onSizeChanged { size ->
                    boxWidth = size.width.toFloat()
                    boxHeight = size.height.toFloat()
                }
        ) {
            val scaleFactor = calculateScaleFactor(balls.size)

            currentTouch?.let { touchInfo ->
                drawLine(
                    color = Color(173, 223, 255),
                    start = Offset(touchInfo.position.x, touchInfo.position.y),
                    end = Offset(touchInfo.position.x, boxHeight),
                    strokeWidth = 10f
                )

                drawBall(
                    Ball(
                        touchInfo.position.x,
                        touchInfo.position.y,
                        gameState.getRadiusWithMass(touchInfo.ballType.mass),
                        touchInfo.ballType,
                        mass = touchInfo.ballType.mass.toFloat()
                    ),
                    painters[touchInfo.ballType] ?: return@Canvas,
                    1f,
                    textMeasurer,
                    0.5f
                )
            }

            balls.forEach { ball ->
                drawBall(ball, painters[ball.type] ?: return@Canvas, scaleFactor, textMeasurer)
            }
            particles.forEach { particle ->
                drawParticle(particle, painters[particle.type]!!)
            }
        }
    }
}

fun Ball.update(
    boxWidth: Float,
    boxHeight: Float,
    gravity: Float,
    friction: Float,
    bounceFactor: Float,
    scaleFactor: Float
) {
    velocityY += gravity * 0.016f
    x += velocityX
    y += velocityY

    velocityX *= friction
    velocityY *= friction

    val scaledRadius = radius * scaleFactor
    // Обработка столкновений с границами экрана
    if (x - scaledRadius < 0) {
        x = scaledRadius
        velocityX = abs(velocityX) * bounceFactor
    } else if (x + scaledRadius > boxWidth) {
        x = boxWidth - scaledRadius
        velocityX = -abs(velocityX) * bounceFactor
    }

    if (y + scaledRadius > boxHeight) {
        y = boxHeight - scaledRadius
        velocityY = -abs(velocityY) * bounceFactor
    } else if (y - scaledRadius < 0) {
        y = scaledRadius
        velocityY = abs(velocityY) * bounceFactor
    }

    // Дополнительная проверка, чтобы шар не застрял в углах
    x = x.coerceIn(scaledRadius, boxWidth - scaledRadius)
    y = y.coerceIn(scaledRadius, boxHeight - scaledRadius)
}


fun calculateScaleFactor(ballCount: Int): Float {
    return max(0.5f, min(1f, 1000f / (ballCount + 1000f)))
}


fun DrawScope.drawBall(
    ball: Ball,
    painter: Painter,
    scaleFactor: Float,
    textMeasurer: TextMeasurer,
    alpha: Float = 1f
) {
    val scaledRadius = ball.radius * scaleFactor
    translate(left = ball.x - scaledRadius, top = ball.y - scaledRadius) {
        clipPath(
            Path().apply {
                addOval(
                    androidx.compose.ui.geometry.Rect(
                        0f,
                        0f,
                        scaledRadius * 2,
                        scaledRadius * 2
                    )
                )
            }
        ) {
            with(painter) {
                draw(
                    size = Size(scaledRadius * 2, scaledRadius * 2),
                    alpha = alpha
                )
            }
            val text = ball.type.name
            val textStyle = TextStyle(
                color = Color.White,
                fontSize = ((ball.radius) / text.length.toFloat()).sp,
                fontWeight = FontWeight.Bold
            )

            val textLayoutResult = textMeasurer.measure(text, textStyle)

            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    scaledRadius - textLayoutResult.size.width / 2,
                    scaledRadius - textLayoutResult.size.height / 2
                ),
                brush = SolidColor(Color.White.copy(alpha = 0.7f))
            )
        }


    }
}

fun DrawScope.drawParticle(
    particle: Particle,
    painter: Painter
) {
    translate(left = particle.x - particle.radius, top = particle.y - particle.radius) {
        clipPath(
            Path().apply {
                addOval(
                    androidx.compose.ui.geometry.Rect(
                        0f,
                        0f,
                        particle.radius * 2,
                        particle.radius * 2
                    )
                )
            }
        ) {
            with(painter) {
                draw(
                    size = Size(particle.radius * 2, particle.radius * 2),
                    alpha = particle.lifespan.coerceIn(0f, 1f)
                )
            }
        }
    }
}

fun Particle.update() {
    x += velocityX
    y += velocityY
    lifespan -= 0.016f
}

fun createParticles(x: Float, y: Float, type: BallType): List<Particle> {
    return List(20) {
        Particle(
            x = x,
            y = y,
            radius = Random.nextFloat() * 5f + 2f,
            type = type,
            velocityX = Random.nextFloat() * 10f - 5f,
            velocityY = Random.nextFloat() * 10f - 5f,
            lifespan = Random.nextFloat() * 0.5f + 0.5f
        )
    }
}

fun handleCollisions(
    balls: MutableList<Ball>,
    particles: MutableList<Particle>,
    gameState: GameState
) {
    for (i in balls.indices) {
        for (j in i + 1 until balls.size) {
            val ball1 = balls[i]
            val ball2 = balls[j]
            val dx = ball2.x - ball1.x
            val dy = ball2.y - ball1.y
            val distance = sqrt(dx * dx + dy * dy)
            val minDistance = (ball1.radius + ball2.radius)// Увеличиваем область проверки

            if (distance < minDistance) {
                // Коррекция позиций
                val angle = atan2(dy, dx)
                val sin = sin(angle)
                val cos = cos(angle)

                // Поворот скоростей
                val vx1 = ball1.velocityX * cos + ball1.velocityY * sin
                val vy1 = ball1.velocityY * cos - ball1.velocityX * sin
                val vx2 = ball2.velocityX * cos + ball2.velocityY * sin
                val vy2 = ball2.velocityY * cos - ball2.velocityX * sin

                // Вычисление коэффициента восстановления на основе масс
                val totalMass = ball1.type.mass + ball2.type.mass
                val restitution = 0.8f * (1f - (totalMass / 100f)).coerceAtLeast(0.2f)

                // Вычисление новых скоростей
                val vFinal1 =
                    ((ball1.type.mass - ball2.type.mass) * vx1 + 2 * ball2.type.mass * vx2) / totalMass
                val vFinal2 =
                    ((ball2.type.mass - ball1.type.mass) * vx2 + 2 * ball1.type.mass * vx1) / totalMass

                // Применение коэффициента восстановления
                val newVx1 = vFinal1 * restitution
                val newVx2 = vFinal2 * restitution

                // Обновление скоростей
                ball1.velocityX = newVx1 * cos - vy1 * sin
                ball1.velocityY = vy1 * cos + newVx1 * sin
                ball2.velocityX = newVx2 * cos - vy2 * sin
                ball2.velocityY = vy2 * cos + newVx2 * sin

                // Разделение шаров
                val overlap = (minDistance - distance) * 0.5f
                ball1.x -= overlap * cos
                ball1.y -= overlap * sin
                ball2.x += overlap * cos
                ball2.y += overlap * sin


                // Проверка на слияние с небольшим допуском
                if (abs(ball1.type.mass - ball2.type.mass) < 0.1f) {
                    val newMass = ball1.type.mass + ball2.type.mass
                    gameState.unlockNewBallType(newMass.toInt())
                    val newType = gameState.getBallTypeForMass(newMass.toInt())
                    val newRadius = gameState.getRadiusWithMass(newMass)
                    val newX = (ball1.x * ball1.type.mass + ball2.x * ball2.type.mass) / newMass
                    val newY = (ball1.y * ball1.type.mass + ball2.y * ball2.type.mass) / newMass
                    val newVelocityX =
                        (ball1.velocityX * ball1.type.mass + ball2.velocityX * ball2.type.mass) / newMass
                    val newVelocityY =
                        (ball1.velocityY * ball1.type.mass + ball2.velocityY * ball2.type.mass) / newMass

                    particles.addAll(createParticles(newX, newY, newType))

                    balls.apply {
                        remove(ball1)
                        remove(ball2)
                        add(Ball(newX, newY, newRadius, newType, newVelocityX, newVelocityY))
                    }

                    //Увеличиваем счетчик очков
                    gameState.score += newMass

                    return // Выходим из функции, так как список шаров изменился
                }
            }
        }
    }
}