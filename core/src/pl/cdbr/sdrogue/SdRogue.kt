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
import com.badlogic.gdx.utils.Array as GdxArray
import pl.cdbr.sdrogue.GameConfig.toAbsValue
import pl.cdbr.sdrogue.game.Engine
import pl.cdbr.sdrogue.game.KbdHandler
import pl.cdbr.sdrogue.game.State

class SdRogue : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var renderer: ShapeRenderer
    lateinit var playerShape: TextureRegion
    lateinit var playerAnims: List<Animation<TextureRegion>>

    private val gc = GameConfig
    private val st = State()
    private val eng = Engine(st, gc)

    private var gridX: Float = 1f
    private var gridY: Float = 1f

    private var timer = 0f
    private var lastStepTimer = timer
    private var xPos = 0
    private var xStep = 0f
    private var moving = false

    override fun create() {
        batch = SpriteBatch()
        renderer = ShapeRenderer()
        val player = Texture(Gdx.files.internal("sprites/player.png"))
        playerShape = TextureRegion(player, 0, 0, 16, 16)
        val allFrames = TextureRegion(player)
        val frames = allFrames.split(16, 16)

        playerAnims = listOf(
                Animation(gc.playerAnimFrameTime, GdxArray(frames[1].sliceArray(1..3))),
                Animation(gc.playerAnimFrameTime, GdxArray(frames[2].sliceArray(1..3)))
        )

        gridX = Gdx.graphics.width / 32f
        gridY = Gdx.graphics.height / 18f

        Gdx.input.inputProcessor = KbdHandler(st)
    }

    override fun render() {
        eng.tick()

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
//        if (!moving && timer - lastStepTimer > 1f) {
//            moving = true
//            lastStepTimer = timer
//        }
//        val frame = if (moving) {
//            val moveTimer = timer - lastStepTimer
//            xStep = (moveTimer / gc.playerMoveTime) //* stepLength == 1
//            if (xStep >= 1f) {
//                moving = false
//                lastStepTimer = timer
//                xStep = 0f
//                xPos += 1
//                if (xPos > 23) { xPos = 0 }
//            }
//            playerAnims[0].getKeyFrame(moveTimer, true)
//        } else { playerShape }
//        batch.draw(
//                frame,
//                (gc.playArea.offX + xPos).toAbsValue(gridX) + (xStep * gridX),
//                gc.playArea.offY.toAbsValue(gridY), gridX, gridY
//        )
        batch.draw(playerShape,
                (gc.playArea.offX + st.player.x).toAbsValue(gridX),
                (gc.playArea.offY + st.player.y).toAbsValue(gridY),
                gridX, gridY
            )
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
