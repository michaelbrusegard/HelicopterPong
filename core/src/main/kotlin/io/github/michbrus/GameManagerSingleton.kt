package io.github.michbrus

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

object GameManagerSingleton {
    private lateinit var batch: SpriteBatch
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var scoreFont: BitmapFont
    private lateinit var renderer: Renderer

    private lateinit var ball: Ball
    private lateinit var leftPaddle: Paddle
    private lateinit var rightPaddle: Paddle
    private lateinit var aiController: AIController

    val score = Score()
    var gameTimer = 0f
    var gameStarted = false
    var gameOver = false

    fun initialize() {
        initializeGraphics()
        initializeGameObjects()
    }

    private fun initializeGraphics() {
        batch = SpriteBatch()
        BackgroundManagerSingleton.initialize()
        shapeRenderer = ShapeRenderer()
        scoreFont = BitmapFont().apply { data.setScale(2f) }
        renderer = Renderer(batch, shapeRenderer, scoreFont, BackgroundManagerSingleton)
    }

    private fun initializeGameObjects() {
        ball = Ball(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        leftPaddle = Paddle(Gdx.graphics.height.toFloat(), true)
        rightPaddle = Paddle(Gdx.graphics.height.toFloat(), false)
        aiController = AIController(rightPaddle)
    }

    fun update(deltaTime: Float) {
        gameTimer += deltaTime

        if (!gameStarted) {
            handleGameStart()
            return
        }

        if (gameOver) {
            handleGameOver()
            return
        }

        updateGameState(deltaTime)
    }

    fun render() {
        renderer.render(ball, leftPaddle, rightPaddle, score, Gdx.graphics.deltaTime, gameStarted, gameOver)
    }

    private fun updateGameState(deltaTime: Float) {
        InputManagerSingleton.handleInput(deltaTime, leftPaddle)
        leftPaddle.update(deltaTime)
        rightPaddle.update(deltaTime)
        aiController.update(deltaTime, ball)

        val scorer = ball.update(deltaTime, leftPaddle, rightPaddle)
        handleScoring(scorer)
    }

    private fun handleScoring(scorer: Int?) {
        when (scorer) {
            1 -> {
                score.player1++
                checkWinCondition(score.player1)
                resetRound()
            }
            2 -> {
                score.player2++
                checkWinCondition(score.player2)
                resetRound()
            }
        }
    }

    private fun checkWinCondition(playerScore: Int) {
        if (playerScore >= GameConfig.WINNING_SCORE) {
            gameOver = true
        }
    }

    private fun handleGameStart() {
        if (Gdx.input.isTouched || InputManagerSingleton.isAnyTouchOrKeyPressed()) {
            gameStarted = true
            resetRound()
        }
    }

    private fun handleGameOver() {
        if (Gdx.input.isTouched || InputManagerSingleton.isAnyTouchOrKeyPressed()) {
            resetGame()
        }
    }

    fun resetRound() {
        gameTimer = 0f
        leftPaddle.height = GameConfig.INITIAL_PADDLE_HEIGHT
        rightPaddle.height = GameConfig.INITIAL_PADDLE_HEIGHT
        ball.reset(gameTimer)
    }

    fun resetGame() {
        score.player1 = 0
        score.player2 = 0
        gameOver = false
        gameStarted = false
        resetRound()
    }

    fun dispose() {
        batch.dispose()
        BackgroundManagerSingleton.dispose()
        shapeRenderer.dispose()
        scoreFont.dispose()
    }

    fun resize(
        width: Int,
        height: Int,
    ) {
        renderer.resize(width, height)
    }
}
