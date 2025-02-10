package io.github.michbrus

interface GameModel {
    val ball: Ball
    val leftPaddle: Paddle
    val rightPaddle: Paddle
    val score: Score
    var gameTimer: Float
    var gameStarted: Boolean
    var gameOver: Boolean

    fun update(deltaTime: Float)

    fun resetRound()

    fun resetGame()
}

class GameModelImpl(
    private val gameWidth: Float,
    private val gameHeight: Float,
) : GameModel {
    override val ball = Ball(gameWidth, gameHeight)
    override val leftPaddle = Paddle(gameHeight, true)
    override val rightPaddle = Paddle(gameHeight, false)
    override val score = Score()
    override var gameTimer = 0f
    override var gameStarted = false
    override var gameOver = false

    private val aiController = AIController(rightPaddle)

    override fun update(deltaTime: Float) {
        gameTimer += deltaTime

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

    override fun resetRound() {
        gameTimer = 0f
        leftPaddle.height = GameConfig.INITIAL_PADDLE_HEIGHT
        rightPaddle.height = GameConfig.INITIAL_PADDLE_HEIGHT
        ball.reset(gameTimer)
    }

    override fun resetGame() {
        score.player1 = 0
        score.player2 = 0
        gameOver = false
        gameStarted = false
        resetRound()
    }
}
