package pl.cdbr.sdrogue

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
}