package pl.cdbr.sdrogue.game.map

import pl.cdbr.sdrogue.GameConfig
import java.util.*

data class Level(val num: Int, val type: LevelType, val seed: Long? = null) {
    fun generateMap() = when (type) {
        LevelType.DUNGEON -> dungeonMap()
        LevelType.ARENA -> arenaMap()
        LevelType.BOSS -> bossMap()
        LevelType.CAVERN -> cavernMap()
    }

    private fun bossMap() = dungeonMap()

    private fun arenaMap() = parametrizedMap(1, (GameConfig.levelBaseSize + num * GameConfig.levelScalingFactor) / 3)
    private fun dungeonMap() = parametrizedMap(80, 6)
    private fun cavernMap() = parametrizedMap(50, 4)

    private fun parametrizedMap(roomDens: Int, roomSize: Int): GameMap {
        val levelSeed = seed ?: Random().nextLong()
        val mapSizeX = GameConfig.levelBaseSize + num * GameConfig.levelScalingFactor
        val mapSizeY = mapSizeX * 2 / 3
        val gen = Generator(mapSizeX, mapSizeY, roomDens, roomSize, levelSeed)
        // starting position always in the middle of the generated map (for now)
        return GameMap(mapSizeX, mapSizeY, gen.startX, gen.startY, mapOf(
                LayerId.GROUND to gen.ground()
        ))
    }
}

enum class LevelType {
    DUNGEON, ARENA, BOSS, CAVERN
}
