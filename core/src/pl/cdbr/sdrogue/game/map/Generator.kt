package pl.cdbr.sdrogue.game.map

import java.util.*
import kotlin.math.roundToInt

@Suppress("unused")
class Generator(
        private val sizeX: Int,
        private val sizeY: Int,
        private val roomDensity: Int,
        private val roomSize: Int
) {
    val startX = sizeX / 2
    val startY = sizeY / 2
    private val rnd = Random()

    enum class Where { IN, WALL, OUT, ACC, WORK, MARK }

    fun ground(): MapLayer {
        println("Generating ground layer with size: $sizeX x $sizeY")
        val roomCount = ((sizeX / 6) * (sizeY / 6) * roomDensity / 100) * (60 + rnd.nextInt(40)) / 100
        println("Room count is $roomCount")

        val floorLayout = createRoomsLayout(roomCount)

        //ensure every floor square is accessible
        //start in the middle (player starting position)
        floorLayout[startY * sizeX + startX] = Where.ACC
        //mark all accessible floor squares
        expandAccessible(floorLayout, Where.IN, Where.ACC)

        //find inaccesible tiles and connect them to the rest
        var inAccessTileIdx = floorLayout.indexOfFirst { it == Where.IN }
        while (inAccessTileIdx > -1) { //there still are inaccessible rooms
            println("Map STILL contains INACCESSIBLE locations!!! (idx: $inAccessTileIdx)")
            //mark "inAccessTile" as WORKing area
            floorLayout[inAccessTileIdx] = Where.WORK
            //mark tiles connected to "inAccessTile" as a working region
            expandAccessible(floorLayout, Where.IN, Where.WORK)
            //find the geometric center of the WORK area
            val centerCoords = findCenterOf(floorLayout, Where.WORK)
            //find an accessible tile that is the nearest to the center
            val nearestAcc = findNearestTo(floorLayout, centerCoords, Where.ACC)
            if (nearestAcc != null) {
                //now pick a WORK tile nearest to the "nearestAcc"
                val nearestWork = findNearestTo(floorLayout, nearestAcc, Where.WORK)
                if (nearestWork != null) {
                    corridorBetween(floorLayout, nearestAcc, nearestWork)
                    //now, the whole "WORK" area is accesible, so mark it out as such...
                    expandAccessible(floorLayout, Where.WORK, Where.ACC)
                } //else shouldn't happen EVER - we already worked it out from the oter side!
            }
            //update the inaccesibe room indicator
            inAccessTileIdx = floorLayout.indexOfFirst { it == Where.IN }
        }

        val layer = MapLayer(sizeX, sizeY, floorLayout.map {
            when (it) {
                Where.IN -> MapTile.DIRT
                Where.ACC -> MapTile.FLOOR_STONE
                Where.WALL -> MapTile.WALL
                Where.MARK -> MapTile.DOOR_O
                else -> MapTile.GRASS
            }
        })
        println(layer)
        return layer
    }

    private fun corridorBetween(fl: Array<Where>, t1: Pair<Int, Int>, t2: Pair<Int, Int>) {
        var (x1, y1) = t1
        val (x2, y2) = t2

        //find and remember the path between t1 and t2
        val path = mutableListOf<Pair<Int, Int>>()
        while (x1 != x2 || y1 != y2) {
            //randomize x/y preference (otherwise the corridor has only one turn...)
            val dir = rnd.nextBoolean()
            if (dir) {
                if (x1 != x2) { //move in X direction
                    x1 -= x1.compareTo(x2)
                } else { //move in Y
                    y1 -= y1.compareTo(y2)
                }
            } else {
                if (y1 != y2) { //move in Y direction
                    y1 -= y1.compareTo(y2)
                } else { //move in X
                    x1 -= x1.compareTo(x2)
                }
            }
            //don't add the final tile to the path (it's already WORKed at)
            if ((x1 != x2 || y1 != y2)) path += (x1 to y1)
        }
        //now draw walls around the path...
        path.forEach { p ->
            p.around().forEach { w ->
                //.. but only on the "outside" tiles
                if (fl[w.toIndex()] == Where.OUT) { fl[w.toIndex()] = Where.WALL }
            }
        }
        // finally, draw the path between walls
        path.forEach { p -> fl[p.toIndex()] = Where.WORK }
    }

    private fun Pair<Int, Int>.around() =
        aroundCoords.map { this.first + it.first to this.second + it.second }
                .filter {
                    it.first in 0..(sizeX - 1) &&
                    it.second in 0..(sizeY - 1)
                }

    private fun Int.toCoords() = this % sizeX to this / sizeX
    private fun Pair<Int, Int>.toIndex() = this.first + this.second * sizeX
    private fun Pair<Int, Int>.distanceTo(to: Pair<Int, Int>) =
            Math.hypot(to.first.toDouble() - this.first, to.second.toDouble() - this.second).roundToInt()

    private fun findNearestTo(fl: Array<Where>, cc: Pair<Int, Int>, what: Where): Pair<Int, Int>? {
        val nearest = fl
                // find distances between cc and all tiles of type "what"
                .mapIndexedNotNull { i, w -> if (w == what) {
                    val iCoords = i.toCoords()
                    iCoords to (cc.distanceTo(iCoords))
                } else { null } }
                // then get the lowest value
                .minBy { it.second }
        //possibly null, because things...
        return nearest?.first
    }

    private fun findCenterOf(fl: Array<Where>, what: Where): Pair<Int, Int> {
        //find all indexes in the array that are "what"
        val idxs = fl.mapIndexedNotNull { i, w -> if (w == what) { i } else { null } }
        // calculate averaged coordinates (middle) of the whole area
        val avgX = idxs.map { it % sizeX }.average().roundToInt()
        val avgY = idxs.map { it / sizeX }.average().roundToInt()
        return avgX to avgY
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
                    if (arrPos < layout.size && currWhere < layout[arrPos]) {
                        layout[arrPos] = currWhere
                    }
                }
            }
        }
        return layout
    }

    private fun expandAccessible(io: Array<Where>, from: Where, to: Where): Boolean {
        var expanded = true
        var updatedCells = 0
        while (expanded) {
            expanded = false
            for (f in io.indices) {
                val (x, y) = f.toCoords()
                if (io[f] == from && (
                    (y > 0 && io[f - sizeX] == to) ||
                    (y < (sizeY - 1) && io[f + sizeX] == to) ||
                    (x > 0 && io[f - 1] == to) ||
                    (x < (sizeX - 1) && io[f + 1] == to)
                )) {
                    io[f] = to
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

    companion object {
        val aroundCoords = listOf(
            -1 to -1, -1 to 0, -1 to 1,
            0 to -1, 0 to 1,
            1 to -1, 1 to 0, 1 to 1
        )
    }
}