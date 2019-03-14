package pl.cdbr.sdrogue.game.map

import com.badlogic.gdx.graphics.Color

data class GameMap(val sx: Int, val sy: Int, val startX: Int, val startY: Int, val layers: Map<LayerId, MapLayer>) {
    fun canGoTo(x: Int, y: Int): Boolean {
        return if (x < 0 || y < 0 || x >= sx || y >= sy) {
            false
        } else {
            layers[LayerId.GROUND]?.tileAt(x, y)?.passable ?: false
        }
    }
}

class MapLayer(private val sx: Int, private val sy: Int, private val tiles: List<MapTile>) {
    fun tileAt(x: Int, y: Int): MapTile? {
        return if (x >= sx || y >= sy) {
            null
        } else {
            tiles[y * sx + x]
        }
    }

    override fun toString(): String {
        return (sy - 1 downTo 0).joinToString("\n") { y ->
            tiles.slice(y * sx until y * sx + sx).joinToString("")
        }
    }
}

enum class LayerId {
    GROUND, ITEMS, MOBS
}

enum class MapTile(val glyph: String, val color: Color, val passable: Boolean = true) {
    GRASS(" ", Color.OLIVE),
    WALL("#", Color.DARK_GRAY, false),
    DIRT("^", Color.BROWN),
    FLOOR_STONE(".", Color.LIGHT_GRAY),
    DOOR_C("+", Color.ROYAL, false),
    DOOR_O("'", Color.ROYAL),

    PLAYER("@", Color.ORANGE),
    DRAGON("D", Color.RED),

    POTION("!", Color.GREEN),
    SWORD("\\", Color.BLUE),
    BOW("}", Color.CHARTREUSE),
    STAFF("/", Color.YELLOW),
    ;

    override fun toString() = glyph
}

