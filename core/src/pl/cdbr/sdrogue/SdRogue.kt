package pl.cdbr.sdrogue

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import pl.cdbr.sdrogue.GameConfig.toAbsValue
import pl.cdbr.sdrogue.game.*
import pl.cdbr.sdrogue.game.graphics.Shapes
import pl.cdbr.sdrogue.game.input.InputFlag
import pl.cdbr.sdrogue.game.input.InputHandler
import pl.cdbr.sdrogue.game.input.MultiInputProcessor
import com.badlogic.gdx.utils.Array as GdxArray

class SdRogue(private vararg val inp: InputHandler) : ApplicationAdapter() {
    private lateinit var batch: SpriteBatch
    private lateinit var renderer: ShapeRenderer
    private lateinit var shapes: Shapes

    private val gc = GameConfig
    private val st = State()
    private val eng = Engine(st)
    private val multiInput = MultiInputProcessor(inp.toList())

    private var gridX: Float = 1f
    private var gridY: Float = 1f

    private var timer = 0f
//    private var lastStepTimer = timer
//    private var xPos = 0
//    private var xStep = 0f
//    private var moving = false

    override fun create() {
        batch = SpriteBatch()
        renderer = ShapeRenderer()
        shapes = Shapes(gc)

        gridX = Gdx.graphics.width / 32f
        gridY = Gdx.graphics.height / 18f
        inp.forEach { it.consumers += st }

        Gdx.input.inputProcessor = multiInput
    }

    override fun render() {
        eng.tick()

        Gdx.gl.glClearColor(0.7f, 0.7f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        timer += Gdx.graphics.deltaTime
        renderer.begin(ShapeRenderer.ShapeType.Filled)

        coloredRegion(gc.inventoryArea, Color.BLACK)
        coloredRegion(gc.messageArea, Color.BROWN)

        val (viewX, viewY) = st.mapOffset()

        gc.playArea.apply {
            val gridCount = this.width * this.height
            for (tn in 0..gridCount) {
                val tx = tn % this.width
                val ty = tn / this.width
                val tile = GameConfig.ScreenRegion(this.offX + tx, this.offY + ty, 1, 1)
                val col = st.mapColorAt(tx + viewX, ty + viewY)
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
        batch.draw(shapes.player,
                (gc.playArea.offX + st.player.x - viewX).toAbsValue(gridX),
                (gc.playArea.offY + st.player.y - viewY).toAbsValue(gridY),
                gridX, gridY
            )

        drawKeyState(batch)

        batch.end()
    }

    private fun drawKeyState(batch: SpriteBatch) {
        if (st.inputFlags.contains(InputFlag.SHIFT)) batch.draw(shapes.keys[Shapes.Key.SHIFT],
                (gc.messageArea.offX + 0).toAbsValue(gridX),
                (gc.messageArea.offY + 0).toAbsValue(gridY),
                gridX, gridY
        )
        if (st.inputFlags.contains(InputFlag.CTRL)) batch.draw(shapes.keys[Shapes.Key.CTRL],
                (gc.messageArea.offX + 1).toAbsValue(gridX),
                (gc.messageArea.offY + 0).toAbsValue(gridY),
                gridX, gridY
        )
        if (st.inputFlags.contains(InputFlag.ALT)) batch.draw(shapes.keys[Shapes.Key.ALT],
                (gc.messageArea.offX + 2).toAbsValue(gridX),
                (gc.messageArea.offY + 0).toAbsValue(gridY),
                gridX, gridY
        )

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
