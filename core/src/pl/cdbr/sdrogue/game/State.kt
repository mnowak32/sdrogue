package pl.cdbr.sdrogue.game

import com.badlogic.gdx.graphics.Color
import pl.cdbr.sdrogue.GameConfig
import pl.cdbr.sdrogue.game.map.LayerId
import pl.cdbr.sdrogue.game.map.Level
import pl.cdbr.sdrogue.game.map.MapLayer
import java.util.*

@Suppress("unused")
class State {
    private val inputQueue: LinkedList<InputEvent> = LinkedList()
    var currentTick: Long = 0L
    private var isDirty: Boolean = false

    private var level: Level = GameConfig.levels.first()
    var map = level.generateMap()

    var player: Player = Player(map.startX, map.startY)
    var mobs: List<Mob> = emptyList()

    class Player(var x: Int, var y: Int) {
        var heading = 0
    }

    abstract class Mob {
        var x = 0
        var y = 0
    }

    fun move(dX: Int, dY: Int) {
        if (map.canGoTo(player.x + dX, player.y + dY)) {
            player.x += dX
            player.y += dY
        } else {
            //???
            // bump off the wall
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

    fun advanceToNextLevel(): Boolean {
        val currLevel = level.num
        val nextLevel = GameConfig.levels.find { it.num == (currLevel + 1) }
        return if (nextLevel != null) {
            level = nextLevel
            map = level.generateMap()
            player.x = map.startX
            player.y = map.startY
            true
        } else {
            false
        }
    }

    fun mapColorAt(tx: Int, ty: Int): Color {
        return map.layers[LayerId.GROUND]?.tileAt(tx, ty)?.color ?: Color.BLACK
    }
}