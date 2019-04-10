package pl.cdbr.sdrogue

import pl.cdbr.sdrogue.game.map.Level
import pl.cdbr.sdrogue.game.map.LevelType

object GameConfig {
    // dimensions in a grid unit (the whole screen is 32x18 units - 16:9 AR)
    // origin is bottom-left!

    // main play area (top left, extending the majority of the screen)
    val playArea: ScreenRegion = ScreenRegion(0, 2, 24, 16)
    // inventory and command area - right sidebar
    val inventoryArea: ScreenRegion = ScreenRegion(24, 2, 8, 16)
    // messages area - screen bottom
    val messageArea: ScreenRegion = ScreenRegion(0, 0, 32, 2)

    //animation parameters
    const val playerAnimFrameTime = 0.06f
//    const val playerMoveTime = 0.24f


    class ScreenRegion(val offX: Int, val offY: Int, val width: Int, val height: Int) {
        val maxX = offX + width - 1
        val maxY = offY + height - 1

        fun scaled(gridX: Float, gridY: Float) = ScaledScreenRegion(this, gridX, gridY)
    }

    class ScaledScreenRegion(val sr: ScreenRegion, val gridX: Float, val gridY: Float) {
        val offX = sr.offX.toAbsValue(gridX)
        val offY = sr.offY.toAbsValue(gridY)
        val width = sr.width.toAbsValue(gridX)
        val height = sr.height.toAbsValue(gridY)

        fun at(x: Int, y: Int): Pair<Float, Float> {
            return (sr.offX + x).toAbsValue(gridX) to (sr.offY + y).toAbsValue(gridY)
        }
        fun atRev(x: Int, y: Int): Pair<Float, Float> {
            return (sr.maxX - x).toAbsValue(gridX) to (sr.maxY - y).toAbsValue(gridY)
        }
    }

    fun Int.toAbsValue(gridDim: Float) = this * gridDim

    val levels = listOf(
            Level(0, LevelType.DUNGEON, -0x42d2b5b5629cae34),
            Level(1, LevelType.CAVERN),
            Level(2, LevelType.DUNGEON),
            Level(3, LevelType.ARENA),
            Level(4, LevelType.DUNGEON),
            Level(5, LevelType.CAVERN),
            Level(6, LevelType.CAVERN),
            Level(7, LevelType.ARENA),
            Level(8, LevelType.DUNGEON),
            Level(9, LevelType.CAVERN),
            Level(10, LevelType.DUNGEON),
            Level(11, LevelType.ARENA),
            Level(12, LevelType.CAVERN),
            Level(13, LevelType.DUNGEON),
            Level(14, LevelType.CAVERN),
            Level(15, LevelType.BOSS)
    )

    const val levelBaseSize = 24
    const val levelScalingFactor = 4
}