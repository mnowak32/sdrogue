package pl.cdbr.sdrogue

import pl.cdbr.sdrogue.game.map.Level
import pl.cdbr.sdrogue.game.map.LevelType
import java.util.*

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
    val playerAnimFrameTime = 0.06f
    val playerMoveTime = 0.24f


    data class ScreenRegion(val offX: Int, val offY: Int, val width: Int, val height: Int) {

    }
    fun Int.toAbsValue(gridDim: Float) = this * gridDim

    val levels = listOf(
//            Level(0, LevelType.RANDOM),
//            Level(1, LevelType.RANDOM),
//            Level(2, LevelType.RANDOM),
//            Level(3, LevelType.ARENA),
            Level(4, LevelType.RANDOM),
            Level(5, LevelType.RANDOM),
            Level(6, LevelType.RANDOM),
            Level(7, LevelType.BOSS)
    )

    val levelBaseSize = 24
    val levelScalingFactor = 2
}