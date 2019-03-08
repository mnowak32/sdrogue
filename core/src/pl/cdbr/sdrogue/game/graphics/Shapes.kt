package pl.cdbr.sdrogue.game.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import pl.cdbr.sdrogue.GameConfig

class Shapes(private val gc: GameConfig) {

    var player: TextureRegion
    var playerAnims: List<Animation<TextureRegion>>
    val keys: Map<Key, TextureRegion>

    enum class Key { SHIFT, CTRL, ALT }

    init {
        val player = Texture(Gdx.files.internal("sprites/player.png"))
        this.player = TextureRegion(player, 0, 0, 16, 16)
        val allFrames = TextureRegion(player)
        val frames = allFrames.split(16, 16)

        playerAnims = listOf(
                Animation(gc.playerAnimFrameTime, Array(frames[1].sliceArray(1..3))),
                Animation(gc.playerAnimFrameTime, Array(frames[2].sliceArray(1..3)))
        )

        val allKeys = Texture(Gdx.files.internal("sprites/keys.png"))
        keys = mapOf(
                Key.SHIFT to TextureRegion(allKeys, 0, 0, 16, 16),
                Key.CTRL to TextureRegion(allKeys, 16, 0, 16, 16),
                Key.ALT to TextureRegion(allKeys, 32, 0, 16, 16)
        )

    }
}