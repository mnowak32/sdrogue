package pl.cdbr.sdrogue

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import pl.cdbr.sdrogue.GameConfig.toAbsValue

class SdRogue : ApplicationAdapter() {
//    lateinit var batch: SpriteBatch
    lateinit var renderer: ShapeRenderer

    val gc = GameConfig

    private var gridX: Float = 1f
    private var gridY: Float = 1f

    override fun create() {
//        batch = SpriteBatch()
        renderer = ShapeRenderer()
        gridX = Gdx.graphics.width / 32f
        gridY = Gdx.graphics.height / 18f

    }

    override fun render() {
        Gdx.gl.glClearColor(0.7f, 0.7f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
//        batch.begin()
        renderer.begin(ShapeRenderer.ShapeType.Filled)

        coloredRegion(gc.inventoryArea, Color.GREEN)
        coloredRegion(gc.messageArea, Color.BROWN)

        gc.playArea.apply {
            val gridCount = this.width * this.height
            for (tn in 0..gridCount) {
                val tx = tn % this.width
                val ty = tn / this.width
                val tile = GameConfig.ScreenRegion(this.offX + tx, this.offY + ty, 1, 1)
                val col = if ((tx + ty) % 2 == 0) { Color.DARK_GRAY } else { Color.WHITE }
                coloredRegion(tile, col)
            }
        }

//        batch.end()
        renderer.end()
    }

    private fun coloredRegion(reg: GameConfig.ScreenRegion, c: Color) {
        renderer.color = c
        renderer.rect(reg.offX.toAbsValue(gridX), reg.offY.toAbsValue(gridY),
                reg.width.toAbsValue(gridX), reg.height.toAbsValue(gridY)
        )
    }

    override fun dispose() {
//        batch.dispose()
        renderer.dispose()
    }
}
