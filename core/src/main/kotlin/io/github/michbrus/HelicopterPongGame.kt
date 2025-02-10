package io.github.michbrus

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx

class HelicopterPongGame : ApplicationAdapter() {
    override fun create() {
        GameManagerSingleton.initialize()
    }

    override fun render() {
        GameManagerSingleton.update(Gdx.graphics.deltaTime)
        GameManagerSingleton.render()
    }

    override fun dispose() {
        GameManagerSingleton.dispose()
    }

    override fun resize(
        width: Int,
        height: Int,
    ) {
        GameManagerSingleton.resize(width, height)
    }
}
