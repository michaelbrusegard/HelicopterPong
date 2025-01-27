package io.github.michbrus

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class HelicopterPongGame : ApplicationAdapter() {
    private val batch by lazy { SpriteBatch() }
    private val backgroundManager = BackgroundManager()
    private lateinit var shapeRenderer: ShapeRenderer
    private lateinit var scoreFont: BitmapFont
    private lateinit var renderer: Renderer

    private lateinit var ball: Ball
    private lateinit var leftPaddle: Paddle
    private lateinit var rightPaddle: Paddle
    private lateinit var aiController: AIController
    private val score = Score()
    private var gameTimer = 0f
    private var gameStarted = false

    override fun create() {
        initializeGraphics()
        initializeGameObjects()
    }

    private fun initializeGraphics() {
        backgroundManager.initialize()
        shapeRenderer = ShapeRenderer()
        scoreFont = BitmapFont().apply { data.setScale(2f) }
        renderer = Renderer(batch, shapeRenderer, scoreFont, backgroundManager)
    }

    private fun initializeGameObjects() {
        ball = Ball(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        leftPaddle = Paddle(Gdx.graphics.height.toFloat(), true)
        rightPaddle = Paddle(Gdx.graphics.height.toFloat(), false)
        aiController = AIController(rightPaddle)
    }

    override fun render() {
        val deltaTime = Gdx.graphics.deltaTime
        gameTimer += deltaTime

        update(deltaTime)
        renderer.render(ball, leftPaddle, rightPaddle, score, deltaTime, gameStarted)
    }

    private fun update(deltaTime: Float) {
        if (!gameStarted) {
            if (Gdx.input.isTouched || Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.ANY_KEY)) {
                gameStarted = true
            }
            return
        }

        handleInput(deltaTime)
        leftPaddle.update(deltaTime)
        rightPaddle.update(deltaTime)
        aiController.update(deltaTime, ball, Gdx.graphics.width.toFloat())

        val scorer = ball.update(deltaTime, leftPaddle, rightPaddle)
        when (scorer) {
            1 -> {
                score.player1++
                resetRound()
            }
            2 -> {
                score.player2++
                resetRound()
            }
        }
    }

    private fun handleInput(deltaTime: Float) {
        if (Gdx.input.isTouched) {
            val touchY = Gdx.graphics.height - Gdx.input.y.toFloat()
            if (Gdx.input.x < Gdx.graphics.width / 2) {
                val targetY = touchY - leftPaddle.height / 2
                if (Math.abs(leftPaddle.y - targetY) > 10) {
                    if (leftPaddle.y < targetY) {
                        leftPaddle.move(GameConfig.PADDLE_SPEED * deltaTime)
                    } else {
                        leftPaddle.move(-GameConfig.PADDLE_SPEED * deltaTime)
                    }
                }
            }
        }

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) {
            leftPaddle.move(GameConfig.PADDLE_SPEED * deltaTime)
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) {
            leftPaddle.move(-GameConfig.PADDLE_SPEED * deltaTime)
        }
    }

    private fun resetRound() {
        gameTimer = 0f
        leftPaddle.height = GameConfig.INITIAL_PADDLE_HEIGHT
        rightPaddle.height = GameConfig.INITIAL_PADDLE_HEIGHT
        ball.reset(gameTimer)
    }

    override fun dispose() {
        batch.dispose()
        backgroundManager.dispose()
        shapeRenderer.dispose()
        scoreFont.dispose()
    }
}
