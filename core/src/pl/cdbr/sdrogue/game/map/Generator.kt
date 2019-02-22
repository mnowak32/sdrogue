package pl.cdbr.sdrogue.game.map

import java.util.*

@Suppress("unused")
class Generator(private val sizeX: Int, private val sizeY: Int, private val roomDensity: Int, val roomSize: Int, val passageDensity: Int) {
    val startX = sizeX / 2
    val startY = sizeY / 2
    private val rnd = Random()

    enum class Where { IN, WALL, OUT }

    fun ground(): MapLayer {
        println("Generating ground layer with size: $sizeX x $sizeY")
        val roomCount = ((sizeX / 6) * (sizeY / 6) * roomDensity / 100) * (60 + rnd.nextInt(40)) / 100
        println("Room count is $roomCount")
        val avgSize = 2 + rnd.nextInt(roomSize)
        println("Average size is $avgSize")

        //ensure there's a room at the starting position
        val rooms = randomRooms(roomCount, avgSize) + randomRoomAt(startX, startY, avgSize)
        println("Created ${rooms.size} rooms")
        val insideOut = Array(sizeX * sizeY) { Where.OUT }
        rooms.forEach { r ->
            for (x in r.xs .. (r.xs + r.x)) {
                for (y in r.ys .. (r.ys + r.y)) {
                    val arrPos = y * sizeX + x
                    val currWhere = when {
                        x == r.xs || y == r.ys -> Where.WALL
                        x == (r.xs + r.x) || y == (r.ys + r.y) -> Where.WALL
                        else -> Where.IN
                    }
                    if (currWhere < insideOut[arrPos]) {
                        insideOut[arrPos] = currWhere
                    }
                }
            }
        }

        val layer = MapLayer(sizeX, sizeY, insideOut.map {
            when (it) {
                Where.IN -> MapTile.FLOOR_STONE
                Where.WALL -> MapTile.WALL
                else -> MapTile.GRASS
            }
        })
        println(layer)
        return layer
    }

    private fun randomRooms(c: Int, avgSize: Int) = (1..c).map {
            val xs = avgSize / 2 + (rnd.nextInt(avgSize - 1)) + 1
            val ys = avgSize / 2 + (rnd.nextInt(avgSize - 1)) + 1
            val x = rnd.nextInt(sizeX - xs)
            val y = rnd.nextInt(sizeY - ys)
            Room(x, y, xs, ys)
        }.toList()

    private fun randomRoomAt(x: Int, y: Int, avgSize: Int): Room {
        val xs = avgSize / 2 + (rnd.nextInt(avgSize - 1))
        val ys = avgSize / 2 + (rnd.nextInt(avgSize - 1))
        val nx = x - xs / 2
        val ny = y - ys / 2
        return Room(nx, ny, xs, ys)
    }

    class Room(val x: Int, val y: Int, val xs: Int, val ys: Int)
}