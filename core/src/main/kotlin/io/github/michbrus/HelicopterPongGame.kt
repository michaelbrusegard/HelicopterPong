package io.github.michbrus

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

class HelicopterPongGame : ApplicationAdapter() {
    private val batch by lazy { SpriteBatch() }
    private val helicopter by lazy { Texture("attackhelicopter.png") }

    private var x = 0f
    private var y = 0f
    private var velocityX = 200f
    private var velocityY = 150f
    private var facingRight = true

    override fun render() {
        x += velocityX * Gdx.graphics.deltaTime
        y += velocityY * Gdx.graphics.deltaTime

        if (x < 0) {
            x = 0f
            velocityX *= -1
            facingRight = velocityX > 0
        } else if (x > Gdx.graphics.width - helicopter.width) {
            x = Gdx.graphics.width - helicopter.width.toFloat()
            velocityX *= -1
            facingRight = velocityX > 0
        }

        if (y < 0) {
            y = 0f
            velocityY *= -1
        } else if (y > Gdx.graphics.height - helicopter.height) {
            y = Gdx.graphics.height - helicopter.height.toFloat()
            velocityY *= -1
        }

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f)
        batch.begin()
        batch.draw(
            helicopter,
            x,
            y,
            helicopter.width.toFloat(),
            helicopter.height.toFloat(),
            0,
            0,
            helicopter.width,
            helicopter.height,
            facingRight,
            false,
        )
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        helicopter.dispose()
    }
}
