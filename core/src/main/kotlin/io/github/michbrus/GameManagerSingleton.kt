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
    private lateinit var model: GameModel

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
        model = GameModelImpl(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        (model.score as? Score)?.addObserver(renderer)
    }

    fun update(deltaTime: Float) {
        if (!model.gameStarted) {
            handleGameStart()
            return
        }

        if (model.gameOver) {
            handleGameOver()
            return
        }

        updateGameState(deltaTime)
    }

    fun render() {
        val gameViewState =
            GameViewState(
                ball = model.ball,
                leftPaddle = model.leftPaddle,
                rightPaddle = model.rightPaddle,
                score = model.score,
                deltaTime = Gdx.graphics.deltaTime,
                gameStarted = model.gameStarted,
                gameOver = model.gameOver,
            )
        renderer.render(gameViewState)
    }

    private fun updateGameState(deltaTime: Float) {
        InputManagerSingleton.handleInput(deltaTime, model.leftPaddle)
        model.update(deltaTime)
    }

    private fun handleGameStart() {
        if (Gdx.input.isTouched || InputManagerSingleton.isAnyInput()) {
            model.gameStarted = true
            model.resetRound()
        }
    }

    private fun handleGameOver() {
        if (Gdx.input.isTouched || InputManagerSingleton.isAnyInput()) {
            model.resetGame()
        }
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
