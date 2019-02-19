package pl.cdbr.sdrogue.desktop

import com.badlogic.gdx.Files
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import pl.cdbr.sdrogue.SdRogue

object DesktopLauncher {
    @JvmStatic
    fun main(arg: Array<String>) {
        val config = LwjglApplicationConfiguration().apply {
            width = 1024
            height = 576
            title = "Software Developer Rogue"
            backgroundFPS = 60
            foregroundFPS = 60
            resizable = false
            addIcon("main icon.png", Files.FileType.Internal)
        }
        LwjglApplication(SdRogue(), config)
    }
}
