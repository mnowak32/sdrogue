package pl.cdbr.sdrogue.game

import java.util.*

class State {
    private val inputQueue: LinkedList<InputEvent> = LinkedList()
    var currentTick: Long = 0L
    var isDirty: Boolean = false

    var map: Map = Map(10, 10, emptyList())
    var player: Player = Player()
    var mobs: List<Mob> = emptyList()

    data class Map(val sx: Int, val sy: Int, val layers: List<MapLayer>) {

    }
    data class MapLayer(val tiles: List<MapTile>)
    abstract class MapTile(val id: String)

    class Player {
        var x = 0
        var y = 0

        var heading = 0
    }

    abstract class Mob {
        var x = 0
        var y = 0
    }



    fun queueEvent(evt: InputEvent) {
        inputQueue.add(evt)
    }

    fun needUpdate() = inputQueue.isNotEmpty()
    fun event(): InputEvent = inputQueue.pop()

    fun dirty() {
        isDirty = true
    }
    fun clean() {
        isDirty = false
    }
}