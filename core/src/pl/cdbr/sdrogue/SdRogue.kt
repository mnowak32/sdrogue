package pl.cdbr.sdrogue

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Array
import pl.cdbr.sdrogue.GameConfig.toAbsValue

class SdRogue : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var renderer: ShapeRenderer
    lateinit var playerShape: TextureRegion
    lateinit var playerAnims: List<Animation<TextureRegion>>

    private val gc = GameConfig

    private var gridX: Float = 1f
    private var gridY: Float = 1f

    private var timer = 0f

    override fun create() {
        batch = SpriteBatch()
        renderer = ShapeRenderer()
        val player = Texture(Gdx.files.internal("sprites/player.png"))
        playerShape = TextureRegion(player, 0, 0, 16, 16)
        val allFrames = TextureRegion(player)
        val frames = allFrames.split(16, 16)

        playerAnims = listOf(
                Animation(0.20f, Array(frames[1].sliceArray(1..3))),
                Animation(0.15f, Array(frames[2].sliceArray(1..3)))
        )

        gridX = Gdx.graphics.width / 32f
        gridY = Gdx.graphics.height / 18f
    }

    override fun render() {
        Gdx.gl.glClearColor(0.7f, 0.7f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        timer += Gdx.graphics.deltaTime
        renderer.begin(ShapeRenderer.ShapeType.Filled)

        coloredRegion(gc.inventoryArea, Color.GREEN)
        coloredRegion(gc.messageArea, Color.BROWN)

        gc.playArea.apply {
            val gridCount = this.width * this.height
            for (tn in 0..gridCount) {
                val tx = tn % this.width
                val ty = tn / this.width
                val tile = GameConfig.ScreenRegion(this.offX + tx, this.offY + ty, 1, 1)
                val col = if ((tx + ty) % 2 == 0) { Color.LIGHT_GRAY } else { Color.WHITE }
                coloredRegion(tile, col)
            }
        }
        renderer.end()

        batch.begin()
        batch.draw(playerShape, gc.playArea.offX.toAbsValue(gridX), gc.playArea.offY.toAbsValue(gridY), gridX, gridY)
        val frame1 = playerAnims[0].getKeyFrame(timer, true)
        batch.draw(frame1, (gc.playArea.offX + 1).toAbsValue(gridX), gc.playArea.offY.toAbsValue(gridY), gridX, gridY)
        val frame2 = playerAnims[1].getKeyFrame(timer, true)
        batch.draw(frame2, (gc.playArea.offX + 2).toAbsValue(gridX), gc.playArea.offY.toAbsValue(gridY), gridX, gridY)
        batch.end()
    }

    private fun coloredRegion(reg: GameConfig.ScreenRegion, c: Color) {
        renderer.color = c
        renderer.rect(reg.offX.toAbsValue(gridX), reg.offY.toAbsValue(gridY),
                reg.width.toAbsValue(gridX), reg.height.toAbsValue(gridY)
        )
    }

    override fun dispose() {
        batch.dispose()
        renderer.dispose()
    }
}
