package io.github.michbrus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.badlogic.gdx.utils.ScreenUtils

interface GameView {
    fun render(gameState: GameViewState)

    fun resize(
        width: Int,
        height: Int,
    )
}

data class GameViewState(
    val ball: Ball,
    val leftPaddle: Paddle,
    val rightPaddle: Paddle,
    val score: Score,
    val deltaTime: Float,
    val gameStarted: Boolean,
    val gameOver: Boolean,
)

class Renderer(
    private val batch: SpriteBatch,
    private val shapeRenderer: ShapeRenderer,
    private val scoreFont: BitmapFont,
    private val backgroundManagerSingleton: BackgroundManagerSingleton,
) : GameView,
    ScoreObserver {
    private val paddleColor = Color(1f, 1f, 1f, 1f)
    private val ballColor = Color(1f, 0.8f, 0.2f, 1f)
    private val startMessageColor = Color(1f, 1f, 1f, 1f)

    private var scoreFlashTimer = 0f
    private var isScoreFlashing = false

    override fun onScoreChanged(
        player1: Int,
        player2: Int,
    ) {
        isScoreFlashing = true
        scoreFlashTimer = 0.5f
    }

    override fun render(gameState: GameViewState) {
        with(gameState) {
            backgroundManagerSingleton.update(deltaTime)
            ScreenUtils.clear(255f / 255f, 24f / 255f, 252f / 255f, 1f)

            renderBackground()
            renderGameObjects(ball, leftPaddle, rightPaddle)
            renderScore(score)

            if (!gameStarted) {
                renderStartMessage()
            } else if (gameOver) {
                renderWinMessage(score)
            }
        }
    }

    private fun renderWinMessage(score: Score) {
        batch.begin()
        scoreFont.setColor(startMessageColor)

        val winner = if (score.player1 > score.player2) "Player" else "Computer"
        val message1 = "$winner Wins!"
        val message2 = "Press any key to restart"

        val screenHeight = Gdx.graphics.height.toFloat()
        val screenWidth = Gdx.graphics.width.toFloat()

        val centerY = screenHeight / 2
        val lineHeight = scoreFont.lineHeight
        val totalHeight = lineHeight * 2
        val startY = centerY + (totalHeight / 2)

        scoreFont.draw(
            batch,
            message1,
            screenWidth / 2 - scoreFont.spaceXadvance * message1.length / 2,
            startY,
        )

        scoreFont.draw(
            batch,
            message2,
            screenWidth / 2 - scoreFont.spaceXadvance * message2.length / 2,
            startY - lineHeight,
        )

        scoreFont.setColor(Color.WHITE)
        batch.end()
    }

    private fun renderStartMessage() {
        batch.begin()
        scoreFont.setColor(startMessageColor)

        val message1 = "Press any key"
        val message2 = "or touch screen to start"

        val screenHeight = Gdx.graphics.height.toFloat()
        val screenWidth = Gdx.graphics.width.toFloat()

        val centerY = screenHeight / 2
        val lineHeight = scoreFont.lineHeight
        val totalHeight = lineHeight * 2
        val startY = centerY + (totalHeight / 2)

        scoreFont.draw(
            batch,
            message1,
            screenWidth / 2 - scoreFont.spaceXadvance * message1.length / 2,
            startY,
        )

        scoreFont.draw(
            batch,
            message2,
            screenWidth / 2 - scoreFont.spaceXadvance * message2.length / 2,
            startY - lineHeight,
        )

        scoreFont.setColor(Color.WHITE)
        batch.end()
    }

    private fun renderBackground() {
        batch.begin()
        backgroundManagerSingleton.render(batch)
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
        shapeRenderer.rectLine(
            ball.x - GameConfig.BALL_SIZE,
            ball.y,
            ball.x + GameConfig.BALL_SIZE,
            ball.y,
            GameConfig.BALL_SIZE * 2,
        )

        shapeRenderer.end()
    }

    private fun renderScore(score: Score) {
        batch.begin()
        if (isScoreFlashing) {
            scoreFlashTimer -= Gdx.graphics.deltaTime
            if (scoreFlashTimer <= 0f) {
                isScoreFlashing = false
            }
            scoreFont.setColor(1f, 1f, 0f, 1f)
        } else {
            scoreFont.setColor(Color.WHITE)
        }

        scoreFont.draw(batch, "${score.player1}", Gdx.graphics.width * 0.25f, Gdx.graphics.height - 50f)
        scoreFont.draw(batch, "${score.player2}", Gdx.graphics.width * 0.75f, Gdx.graphics.height - 50f)

        scoreFont.setColor(Color.WHITE)
        batch.end()
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        batch.projectionMatrix.setToOrtho2D(0f, 0f, width.toFloat(), height.toFloat())
        shapeRenderer.projectionMatrix.setToOrtho2D(0f, 0f, width.toFloat(), height.toFloat())
    }
}
