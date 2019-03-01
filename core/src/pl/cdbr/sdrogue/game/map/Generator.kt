package pl.cdbr.sdrogue.game.map

import java.util.*

@Suppress("unused")
class Generator(
        private val sizeX: Int,
        private val sizeY: Int,
        private val roomDensity: Int,
        private val roomSize: Int,
        private val passageDensity: Int
) {
    val startX = sizeX / 2
    val startY = sizeY / 2
    private val rnd = Random()

    enum class Where { IN, WALL, OUT, ACC }

    fun ground(): MapLayer {
        println("Generating ground layer with size: $sizeX x $sizeY")
        val roomCount = ((sizeX / 6) * (sizeY / 6) * roomDensity / 100) * (60 + rnd.nextInt(40)) / 100
        println("Room count is $roomCount")

        val floorLayout = createRoomsLayout(roomCount)

        //ensure every floor square is accessible
        //start in the middle (player starting position)
        floorLayout[startY * sizeX + startX] = Where.ACC
        //mark all accessible floor squares
        expandAccessible(floorLayout)
        if (floorLayout.contains(Where.IN)) { //there still are inaccessible rooms
            println("Map STILL contains INACCESSIBLE locations!!!")
        }

        val layer = MapLayer(sizeX, sizeY, floorLayout.map {
            when (it) {
                Where.IN -> MapTile.DIRT
                Where.ACC -> MapTile.FLOOR_STONE
                Where.WALL -> MapTile.WALL
                else -> MapTile.GRASS
            }
        })
        println(layer)
        return layer
    }

    private fun createRoomsLayout(roomCount: Int): Array<Where> {
        val layout = Array(sizeX * sizeY) { Where.OUT }
        //some random rooms + ensure there's a room at the starting position
        val rooms = randomRooms(roomCount, roomSize) + randomRoomAt(startX, startY, roomSize)
        println("Created ${rooms.size} rooms")
        rooms.forEach { r ->
            for (x in 0..r.xs) {
                for (y in 0..r.ys) {
                    val arrPos = (y + r.y) * sizeX + x + r.x
                    val currWhere = when {
                        x == 0 || y == 0 -> Where.WALL
                        x == r.xs || y == r.ys -> Where.WALL
                        else -> Where.IN
                    }
                    if (currWhere < layout[arrPos]) {
                        layout[arrPos] = currWhere
                    }
                }
            }
        }
        return layout
    }

    private fun expandAccessible(io: Array<Where>): Boolean {
        var expanded = true
        var updatedCells = 0
        while (expanded) {
            expanded = false
            for (f in io.indices) {
                val x = f % sizeX
                val y = f / sizeX
                if (io[f] == Where.IN && (
                    (y > 0 && io[f - sizeX] == Where.ACC) ||
                    (y < (sizeY - 1) && io[f + sizeX] == Where.ACC) ||
                    (x > 0 && io[f - 1] == Where.ACC) ||
                    (x < (sizeX - 1) && io[f + 1] == Where.ACC)
                )) {
                    io[f] = Where.ACC
                    expanded = true
                    updatedCells++
                }
            }
        }
        return updatedCells > 0
    }

    private fun randomRooms(c: Int, avgSize: Int) = (1..c).map {
            val xs = avgSize / 2 + (rnd.nextInt(avgSize - 1)) + 1
            val ys = avgSize / 2 + (rnd.nextInt(avgSize - 1)) + 1
            val x = rnd.nextInt(sizeX - xs)
            val y = rnd.nextInt(sizeY - ys)
            Room(x, y, xs, ys)
        }.toList()

    private fun randomRoomAt(x: Int, y: Int, avgSize: Int): Room {
        val xs = avgSize / 2 + (rnd.nextInt(avgSize - 1)) + 1
        val ys = avgSize / 2 + (rnd.nextInt(avgSize - 1)) + 1
        val nx = x - xs / 2
        val ny = y - ys / 2
        return Room(nx, ny, xs, ys)
    }

    class Room(val x: Int, val y: Int, val xs: Int, val ys: Int)
}