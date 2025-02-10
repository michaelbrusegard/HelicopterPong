package io.github.michbrus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

object InputManagerSingleton {
    fun handleInput(
        deltaTime: Float,
        leftPaddle: Paddle,
    ) {
        handleTouchInput(deltaTime, leftPaddle)
        handleKeyboardInput(deltaTime, leftPaddle)
    }

    fun isAnyTouchOrKeyPressed(): Boolean = Gdx.input.isTouched || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)

    private fun handleTouchInput(
        deltaTime: Float,
        leftPaddle: Paddle,
    ) {
        if (Gdx.input.isTouched) {
            val touchY = Gdx.graphics.height - Gdx.input.y
            val paddleCenter = leftPaddle.y + leftPaddle.height / 2

            if (Math.abs(touchY - paddleCenter) > 10) {
                if (touchY > paddleCenter) {
                    leftPaddle.move(GameConfig.PADDLE_SPEED * deltaTime)
                } else {
                    leftPaddle.move(-GameConfig.PADDLE_SPEED * deltaTime)
                }
            }
        }
    }

    private fun handleKeyboardInput(
        deltaTime: Float,
        leftPaddle: Paddle,
    ) {
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            leftPaddle.move(GameConfig.PADDLE_SPEED * deltaTime)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            leftPaddle.move(-GameConfig.PADDLE_SPEED * deltaTime)
        }
    }
}
