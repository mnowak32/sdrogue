package pl.cdbr.sdrogue.game.map

import com.badlogic.gdx.graphics.Color

data class GameMap(val sx: Int, val sy: Int, val startX: Int, val startY: Int, val layers: Map<LayerId, MapLayer>) {
    fun canGoTo(x: Int, y: Int) = (x >= 0 && y >= 0 && x < sx && y < sy)
}

class MapLayer(val sx: Int, val sy: Int, val tiles: List<MapTile>) {
    override fun toString(): String {
        return (0 until sy).joinToString("\n") { y ->
            tiles.slice(y * sx until y * sx + sx).joinToString("") { it.glyph }
        }
    }
}

enum class LayerId {
    GROUND, ITEMS, MOBS
}

enum class MapTile(val glyph: String, val color: Color, val passable: Boolean = true) {
    GRASS(",", Color.GREEN),
    WALL("#", Color.DARK_GRAY, false),
    DIRT(".", Color.BROWN),
    FLOOR_STONE(".", Color.LIGHT_GRAY),
    DOOR_C("+", Color.ROYAL, false),
    DOOR_O("'", Color.ROYAL),

    PLAYER("@", Color.ORANGE),
    DRAGON("D", Color.RED),

    POTION("!", Color.GREEN),
    SWORD("\\", Color.BLUE),
    BOW("}", Color.CHARTREUSE),
    STAFF("/", Color.YELLOW),
}

