package pl.cdbr.sdrogue

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
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
    private lateinit var textFont: BitmapFont

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

    private lateinit var invArea: GameConfig.ScaledScreenRegion
    private lateinit var playArea: GameConfig.ScaledScreenRegion
    private lateinit var mesgArea: GameConfig.ScaledScreenRegion

    override fun create() {
        batch = SpriteBatch()
        renderer = ShapeRenderer()
        shapes = Shapes(gc)

        gridX = Gdx.graphics.width / 32f
        gridY = Gdx.graphics.height / 18f
        invArea = gc.inventoryArea.scaled(gridX, gridY)
        playArea = gc.playArea.scaled(gridX, gridY)
        mesgArea = gc.messageArea.scaled(gridX, gridY)

        inp.forEach { it.consumers += st }

        Gdx.input.inputProcessor = multiInput

        val fontGen = FreeTypeFontGenerator(Gdx.files.internal("fonts/EnterCommand-Bold.ttf"))
        textFont = fontGen.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply { size = gridY.toInt() })
        fontGen.dispose()
    }

    override fun render() {
        eng.tick()

        Gdx.gl.glClearColor(0.7f, 0.7f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        timer += Gdx.graphics.deltaTime
        renderer.begin(ShapeRenderer.ShapeType.Filled)

        coloredRegion(invArea, Color.BLACK)
        coloredRegion(playArea, Color.BLACK)
        coloredRegion(mesgArea, Color.BROWN)

        val (viewX, viewY) = st.mapOffset()

        gc.playArea.apply {
            val gridCount = this.width * this.height
            for (tn in 0..gridCount) {
                val tx = tn % this.width
                val ty = tn / this.width
                val tilePos = playArea.at(tx, ty)
                renderer.color = st.mapColorAt(tx + viewX, ty + viewY)
                renderer.rect(tilePos.first, tilePos.second, gridX, gridY)
            }
        }
        renderer.end()

        batch.begin()

//        gc.playArea.apply {
//            val gridCount = this.width * this.height
//            for (tn in 0..gridCount) {
//                val tx = tn % this.width
//                val ty = tn / this.width
//                val charPos = playArea.at(tx, ty + 1)
//                textFont.draw(batch, st.mapCharAt(tx + viewX, ty + viewY), charPos.first + 4, charPos.second - 2)
//            }
//        }

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
        val playerPos = playArea.at(st.player.x - viewX, st.player.y - viewY)
        batch.draw(shapes.player, playerPos.first, playerPos.second, gridX, gridY)

        drawKeyState(batch)

        val textPos = mesgArea.at(0, 2)
        textFont.draw(batch, "Hello Adventurer!", textPos.first, textPos.second)

        batch.end()
    }

    private fun drawKeyState(batch: SpriteBatch) {
        val shiftPos = mesgArea.atRev(2, 0)
        val ctrlPos = mesgArea.atRev(1, 0)
        val altPos = mesgArea.atRev(0, 0)
        if (st.inputFlags.contains(InputFlag.SHIFT)) batch.draw(shapes.keys[Shapes.Key.SHIFT],
                shiftPos.first, shiftPos.second,
                gridX, gridY
        )
        if (st.inputFlags.contains(InputFlag.CTRL)) batch.draw(shapes.keys[Shapes.Key.CTRL],
                ctrlPos.first, ctrlPos.second,
                gridX, gridY
        )
        if (st.inputFlags.contains(InputFlag.ALT)) batch.draw(shapes.keys[Shapes.Key.ALT],
                altPos.first, altPos.second,
                gridX, gridY
        )

    }

    private fun coloredRegion(reg: GameConfig.ScaledScreenRegion, c: Color) {
        renderer.color = c
        renderer.rect(reg.offX, reg.offY, reg.width, reg.height)
    }

    override fun dispose() {
        batch.dispose()
        renderer.dispose()
    }
}
