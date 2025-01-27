package io.github.michbrus

import com.badlogic.gdx.Gdx

class Paddle(
    private val gameHeight: Float,
    private val isLeftPaddle: Boolean,
) {
    var y = gameHeight / 2f - GameConfig.INITIAL_PADDLE_HEIGHT / 2f
    var height = GameConfig.INITIAL_PADDLE_HEIGHT

    val x: Float = if (isLeftPaddle) 20f else Gdx.graphics.width - 30f

    fun update(deltaTime: Float) {
        if (height > GameConfig.MIN_PADDLE_HEIGHT) {
            height -= GameConfig.PADDLE_SHRINK_RATE * deltaTime
        }
        y = y.coerceIn(0f, gameHeight - height)
    }

    fun move(amount: Float) {
        y += amount
        y = y.coerceIn(0f, gameHeight - height)
    }
}
