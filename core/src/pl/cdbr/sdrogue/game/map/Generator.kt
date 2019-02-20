package pl.cdbr.sdrogue.game.map

import java.util.*

@Suppress("unused")
class Generator(private val size: Int, private val roomDensity: Int, val passageDensity: Int) {
    private val mid = size / 2
    private val rnd = Random()

    enum class Where { IN, WALL, OUT }

    fun ground(): MapLayer {
        println("Generating ground layer with size: $size")
        val roomCount = ((size / 5) * (size / 5) * roomDensity / 100) * (50 + rnd.nextInt(50)) / 100
        println("Room count is $roomCount")
        val avgSize = 2 + rnd.nextInt(5)
        println("Average size is $avgSize")

        //ensure there's a room at the starting position
        val rooms = randomRooms(roomCount, avgSize) + randomRoomAt(mid, mid, avgSize)
        println("Created ${rooms.size} rooms")
        val insideOut = Array(size * size) { Where.OUT }
        rooms.forEach { r ->
            for (x in r.xs .. (r.xs + r.x)) {
                for (y in r.ys .. (r.ys + r.y)) {
                    val arrPos = y * size + x
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

        val layer = MapLayer(size, size, insideOut.map {
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
            val x = rnd.nextInt(size - xs)
            val y = rnd.nextInt(size - ys)
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