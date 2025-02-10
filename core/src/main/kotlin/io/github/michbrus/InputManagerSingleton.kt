package io.github.michbrus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input

interface InputController {
    fun handleInput(
        deltaTime: Float,
        paddle: Paddle,
    )

    fun isAnyInput(): Boolean
}

object InputManagerSingleton : InputController {
    override fun handleInput(
        deltaTime: Float,
        paddle: Paddle,
    ) {
        handleTouchInput(deltaTime, paddle)
        handleKeyboardInput(deltaTime, paddle)
    }

    override fun isAnyInput(): Boolean = Gdx.input.isTouched || Gdx.input.isKeyPressed(Input.Keys.ANY_KEY)

    private fun handleTouchInput(
        deltaTime: Float,
        paddle: Paddle,
    ) {
        if (Gdx.input.isTouched) {
            val touchY = Gdx.graphics.height - Gdx.input.y
            val paddleCenter = paddle.y + paddle.height / 2

            if (Math.abs(touchY - paddleCenter) > 10) {
                if (touchY > paddleCenter) {
                    paddle.move(GameConfig.PADDLE_SPEED * deltaTime)
                } else {
                    paddle.move(-GameConfig.PADDLE_SPEED * deltaTime)
                }
            }
        }
    }

    private fun handleKeyboardInput(
        deltaTime: Float,
        paddle: Paddle,
    ) {
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            paddle.move(GameConfig.PADDLE_SPEED * deltaTime)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            paddle.move(-GameConfig.PADDLE_SPEED * deltaTime)
        }
    }
}
