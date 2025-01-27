package io.github.michbrus

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import kotlin.math.abs

class HelicopterPongGame : ApplicationAdapter() {
    private val batch by lazy { SpriteBatch() }
    private val helicopterFrames = mutableListOf<Texture>()
    private val frameTime = 0.1f
    private var stateTime = 0f
    private val helicopters = mutableListOf<Helicopter>()
    private val numHelicopters = 3
    val width = 130f
    val height = 52f

    data class Helicopter(
        var x: Float,
        var y: Float,
        var velocityX: Float,
        var velocityY: Float,
        var facingRight: Boolean = true,
    ) {
        val width = 130f
        val height = 52f

        fun update(delta: Float) {
            x += velocityX * delta
            y += velocityY * delta
        }

        fun bounceOffWalls(
            screenWidth: Float,
            screenHeight: Float,
        ) {
            if (x < 0) {
                x = 0f
                velocityX *= -1
                facingRight = velocityX > 0
            } else if (x > screenWidth - width) {
                x = screenWidth - width
                velocityX *= -1
                facingRight = velocityX > 0
            }

            if (y < 0) {
                y = 0f
                velocityY *= -1
            } else if (y > screenHeight - height) {
                y = screenHeight - height
                velocityY *= -1
            }
        }

        fun checkCollision(other: Helicopter) {
            if (x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y
            ) {
                val overlapX =
                    if (x < other.x) {
                        (x + width) - other.x
                    } else {
                        x - (other.x + other.width)
                    }

                val overlapY =
                    if (y < other.y) {
                        (y + height) - other.y
                    } else {
                        y - (other.y + other.height)
                    }

                if (abs(overlapX) < abs(overlapY)) {
                    if (x < other.x) {
                        x -= overlapX / 2
                        other.x += overlapX / 2
                    } else {
                        x += abs(overlapX) / 2
                        other.x -= abs(overlapX) / 2
                    }
                } else {
                    if (y < other.y) {
                        y -= overlapY / 2
                        other.y += overlapY / 2
                    } else {
                        y += abs(overlapY) / 2
                        other.y -= abs(overlapY) / 2
                    }
                }

                val tempVX = velocityX
                val tempVY = velocityY
                velocityX = other.velocityX
                velocityY = other.velocityY
                other.velocityX = tempVX
                other.velocityY = tempVY

                facingRight = velocityX > 0
                other.facingRight = other.velocityX > 0
            }
        }
    }

    override fun create() {
        for (i in 1..4) {
            helicopterFrames.add(Texture("heli$i.png"))
        }

        repeat(numHelicopters) {
            helicopters.add(
                Helicopter(
                    x = (Math.random() * (Gdx.graphics.width - 130)).toFloat(),
                    y = (Math.random() * (Gdx.graphics.height - 52)).toFloat(),
                    velocityX = (Math.random() * 200 - 100).toFloat(),
                    velocityY = (Math.random() * 200 - 100).toFloat(),
                ),
            )
        }
    }

    override fun render() {
        stateTime += Gdx.graphics.deltaTime
        val currentFrame = ((stateTime / frameTime) % 4).toInt()

        helicopters.forEach { helicopter ->
            helicopter.update(Gdx.graphics.deltaTime)
            helicopter.bounceOffWalls(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        }

        for (i in 0 until helicopters.size - 1) {
            for (j in i + 1 until helicopters.size) {
                helicopters[i].checkCollision(helicopters[j])
            }
        }

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)
        batch.begin()

        helicopters.forEach { helicopter ->
            batch.draw(
                helicopterFrames[currentFrame],
                helicopter.x,
                helicopter.y,
                helicopter.width,
                helicopter.height,
                0,
                0,
                helicopterFrames[currentFrame].width,
                helicopterFrames[currentFrame].height,
                helicopter.facingRight,
                false,
            )
        }

        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        helicopterFrames.forEach { it.dispose() }
    }
}
