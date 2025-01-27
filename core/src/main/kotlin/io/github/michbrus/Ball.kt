package io.github.michbrus

class Ball(
    private val gameWidth: Float,
    private val gameHeight: Float,
) {
    var x = gameWidth / 2f
    var y = gameHeight / 2f
    var velocityX = 300f
    var velocityY = 300f

    fun update(
        deltaTime: Float,
        leftPaddle: Paddle,
        rightPaddle: Paddle,
    ): Int? {
        x += velocityX * deltaTime
        y += velocityY * deltaTime

        handleWallCollisions()
        handlePaddleCollisions(leftPaddle, rightPaddle)

        return when {
            x < 0 -> 2
            x > gameWidth -> 1
            else -> null
        }
    }

    private fun handleWallCollisions() {
        if (y - GameConfig.BALL_SIZE <= 0) {
            y = GameConfig.BALL_SIZE
            velocityY *= -1
        } else if (y + GameConfig.BALL_SIZE >= gameHeight) {
            y = gameHeight - GameConfig.BALL_SIZE
            velocityY *= -1
        }
    }

    private fun handlePaddleCollisions(
        leftPaddle: Paddle,
        rightPaddle: Paddle,
    ) {
        if (x - GameConfig.BALL_SIZE <= 30f &&
            y >= leftPaddle.y &&
            y <= leftPaddle.y + leftPaddle.height
        ) {
            velocityX *= -1.1f
            x = 30f + GameConfig.BALL_SIZE
        }

        if (x + GameConfig.BALL_SIZE >= gameWidth - 30f &&
            y >= rightPaddle.y &&
            y <= rightPaddle.y + rightPaddle.height
        ) {
            velocityX *= -1.1f
            x = gameWidth - 30f - GameConfig.BALL_SIZE
        }
    }

    fun reset(gameTimer: Float) {
        x = gameWidth / 2f
        y = gameHeight / 2f

        val timeMultiplier = 1f + (gameTimer / 10f * GameConfig.SPEED_MULTIPLIER)
        val currentSpeed = GameConfig.BASE_SPEED * timeMultiplier

        velocityX = if (Math.random() > 0.5) currentSpeed else -currentSpeed
        velocityY = if (Math.random() > 0.5) currentSpeed else -currentSpeed
    }
}
