package io.github.michbrus

import kotlin.math.abs

class AIController(
    private val paddle: Paddle,
) {
    fun update(
        deltaTime: Float,
        ball: Ball,
    ) {
        val paddleCenter = paddle.y + paddle.height / 2
        val predictionError = (Math.random() * GameConfig.AI_PREDICTION_ERROR - GameConfig.AI_PREDICTION_ERROR / 2).toFloat()
        val targetY = ball.y + predictionError

        if (ball.velocityX > 0) {
            val distanceToTarget = abs(paddleCenter - targetY)
            val aiSpeed = (300f + distanceToTarget * 0.5f).coerceIn(200f, 400f)

            if (Math.random() > 0.05) {
                when {
                    paddleCenter < targetY - 10 -> paddle.move(aiSpeed * deltaTime)
                    paddleCenter > targetY + 10 -> paddle.move(-aiSpeed * deltaTime)
                }
            }
        }
    }
}
