package io.github.michbrus

interface ScoreObserver {
    fun onScoreChanged(
        player1: Int,
        player2: Int,
    )
}

class Score {
    private val observers = mutableListOf<ScoreObserver>()
    var player1 = 0
        set(value) {
            field = value
            notifyObservers()
        }
    var player2 = 0
        set(value) {
            field = value
            notifyObservers()
        }

    fun addObserver(observer: ScoreObserver) {
        observers.add(observer)
    }

    fun removeObserver(observer: ScoreObserver) {
        observers.remove(observer)
    }

    private fun notifyObservers() {
        observers.forEach { it.onScoreChanged(player1, player2) }
    }
}
