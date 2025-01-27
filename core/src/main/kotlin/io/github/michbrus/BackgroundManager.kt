package io.github.michbrus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class BackgroundManager {
    companion object {
        private const val MIN_SPEED = -100f
        private const val MAX_SPEED = 100f
        private const val FRAME_TIME = 0.1f
    }

    private val helicopterFrames = mutableListOf<Texture>()
    private val helicopters = mutableListOf<Helicopter>()
    private var stateTime = 0f

    private fun generateRandomSpeed(): Float = (MIN_SPEED + Math.random() * (MAX_SPEED - MIN_SPEED)).toFloat()

    fun initialize(numHelicopters: Int = 4) {
        for (i in 1..4) {
            helicopterFrames.add(Texture("heli$i.png"))
        }

        repeat(numHelicopters) {
            helicopters.add(
                Helicopter(
                    x = (Math.random() * (Gdx.graphics.width - 130)).toFloat(),
                    y = (Math.random() * (Gdx.graphics.height - 52)).toFloat(),
                    velocityX = generateRandomSpeed(),
                    velocityY = generateRandomSpeed(),
                ),
            )
        }
    }

    fun update(delta: Float) {
        stateTime += delta

        helicopters.forEach { helicopter ->
            helicopter.update(delta)
            helicopter.bounceOffWalls(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        }

        for (i in 0 until helicopters.size - 1) {
            for (j in i + 1 until helicopters.size) {
                helicopters[i].checkCollision(helicopters[j])
            }
        }
    }

    fun render(batch: SpriteBatch) {
        val currentFrame = ((stateTime / FRAME_TIME) % 4).toInt()

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
    }

    fun dispose() {
        helicopterFrames.forEach { it.dispose() }
    }
}
