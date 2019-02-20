package pl.cdbr.sdrogue.game.map

import pl.cdbr.sdrogue.GameConfig

data class Level(val num: Int, val type: LevelType) {
    fun generateMap() = when (type) {
        LevelType.RANDOM -> randomMap()
        LevelType.ARENA -> arenaMap()
        LevelType.BOSS -> bossMap()
    }

    private fun bossMap() = randomMap()

    private fun arenaMap() = randomMap()

    private fun randomMap(): GameMap {
        val mapSize = GameConfig.levelBaseSize + num * GameConfig.levelScalingFactor
        val gen = Generator(mapSize, 100, 100)
        // starting position always in the middle of the generated map (for now)
        return GameMap(mapSize, mapSize, mapSize / 2, mapSize / 2, mapOf(
                LayerId.GROUND to gen.ground()
        ))
    }
}

enum class LevelType {
    RANDOM, ARENA, BOSS
}
