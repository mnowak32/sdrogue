package pl.cdbr.sdrogue.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import pl.cdbr.sdrogue.SdRogue

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            width = 800
            height = 450
            title = "Software Developer Rogue"
            backgroundFPS = 60
            foregroundFPS = 60
            resizable = false
        }
        LwjglApplication(SdRogue(), config)
    }
}
