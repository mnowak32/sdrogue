package pl.cdbr.sdrogue.game

import java.util.*

@Suppress("unused")
class State {
    private val inputQueue: LinkedList<InputEvent> = LinkedList()
    var currentTick: Long = 0L
    private var isDirty: Boolean = false

    var map: Map = Map(10, 10, 2, 4, emptyList())
    var player: Player = Player(map.startX, map.startY)
    var mobs: List<Mob> = emptyList()

    data class Map(val sx: Int, val sy: Int, val startX: Int, val startY: Int, val layers: List<MapLayer>) {
        fun canGotTo(x: Int, y: Int) = (x >= 0 && y >= 0 && x < sx && y < sy)
    }

    data class MapLayer(val tiles: List<MapTile>)
    abstract class MapTile(val id: String)

    class Player(var x: Int, var y: Int) {
        var heading = 0
    }

    abstract class Mob {
        var x = 0
        var y = 0
    }

    fun move(dX: Int, dY: Int) {
        if (map.canGotTo(player.x + dX, player.y + dY)) {
            player.x += dX
            player.y += dY
        } else {
            //???
        }
    }


    fun queueEvent(evt: InputEvent) {
        inputQueue.add(evt)
    }

    fun eventWaiting() = inputQueue.isNotEmpty()
    fun event(): InputEvent = inputQueue.pop()

    fun dirty() {
        isDirty = true
    }
    fun clean() {
        isDirty = false
    }
}