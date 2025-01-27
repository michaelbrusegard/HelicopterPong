package io.github.michbrus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.utils.ScreenUtils

class Renderer(
    private val batch: SpriteBatch,
    private val shapeRenderer: ShapeRenderer,
    private val scoreFont: BitmapFont,
    private val backgroundManager: BackgroundManager,
) {
    private val paddleColor = Color(1f, 1f, 1f, 1f)
    private val ballColor = Color(1f, 0.8f, 0.2f, 1f)

    fun render(
        ball: Ball,
        leftPaddle: Paddle,
        rightPaddle: Paddle,
        score: Score,
        deltaTime: Float,
    ) {
        backgroundManager.update(deltaTime)
        ScreenUtils.clear(255f / 255f, 24f / 255f, 252f / 255f, 1f)

        renderBackground()
        renderGameObjects(ball, leftPaddle, rightPaddle)
        renderScore(score)
    }

    private fun renderBackground() {
        batch.begin()
        backgroundManager.render(batch)
        batch.end()
    }

    private fun renderGameObjects(
        ball: Ball,
        leftPaddle: Paddle,
        rightPaddle: Paddle,
    ) {
        shapeRenderer.begin(ShapeType.Filled)

        shapeRenderer.setColor(paddleColor)
        shapeRenderer.rect(leftPaddle.x, leftPaddle.y, GameConfig.PADDLE_WIDTH, leftPaddle.height)
        shapeRenderer.rect(rightPaddle.x, rightPaddle.y, GameConfig.PADDLE_WIDTH, rightPaddle.height)

        shapeRenderer.setColor(ballColor)
        shapeRenderer.circle(ball.x, ball.y, GameConfig.BALL_SIZE)

        shapeRenderer.end()
    }

    private fun renderScore(score: Score) {
        batch.begin()
        scoreFont.draw(batch, "${score.player1}", Gdx.graphics.width * 0.25f, Gdx.graphics.height - 50f)
        scoreFont.draw(batch, "${score.player2}", Gdx.graphics.width * 0.75f, Gdx.graphics.height - 50f)
        batch.end()
    }
}
