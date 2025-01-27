package io.github.michbrus

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

class HelicopterPongGame : ApplicationAdapter() {
    private val batch by lazy { SpriteBatch() }
    private val backgroundManager = BackgroundManager()

    override fun create() {
        backgroundManager.initialize()
    }

    override fun render() {
        backgroundManager.update(Gdx.graphics.deltaTime)

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)
        batch.begin()
        backgroundManager.render(batch)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        backgroundManager.dispose()
    }
}
