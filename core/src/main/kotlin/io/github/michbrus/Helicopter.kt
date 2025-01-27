package io.github.michbrus

import kotlin.math.abs

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
