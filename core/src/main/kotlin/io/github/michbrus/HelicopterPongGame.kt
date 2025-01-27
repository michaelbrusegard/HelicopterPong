package io.github.michbrus

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils

class HelicopterPongGame : ApplicationAdapter() {
    private val batch by lazy { SpriteBatch() }
    private val helicopter by lazy { Texture("attackhelicopter.png") }
    private val font by lazy { BitmapFont() }

    private var x = 0f
    private var y = 0f
    private var facingRight = true
    private var lastTouchX = 0f

    override fun render() {
        if (Gdx.input.isTouched) {
            val touchX = Gdx.input.x.toFloat()
            val touchY = Gdx.graphics.height.toFloat() - Gdx.input.y.toFloat()

            if (touchX != lastTouchX) {
                facingRight = touchX > lastTouchX
            }
            lastTouchX = touchX

            x = (touchX - helicopter.width.toFloat() / 2f).coerceIn(0f, Gdx.graphics.width.toFloat() - helicopter.width.toFloat())
            y = (touchY - helicopter.height.toFloat() / 2f).coerceIn(0f, Gdx.graphics.height.toFloat() - helicopter.height.toFloat())
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

        font.draw(batch, "Position: (${x.toInt()}, ${y.toInt()})", 10f, Gdx.graphics.height.toFloat() - 10f)

        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        helicopter.dispose()
        font.dispose()
    }
}
