package pl.cdbr.sdrogue.game.input

import com.badlogic.gdx.InputProcessor

class MultiInputProcessor(inp: Collection<InputHandler>) : InputProcessor {
    private val processors = mutableListOf<InputHandler>()
    init {
        processors += inp
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) =
            processEvent { it.touchUp(screenX, screenY, pointer, button) }

    override fun mouseMoved(screenX: Int, screenY: Int) =
            processEvent { it.mouseMoved(screenX, screenY) }

    override fun keyTyped(character: Char) =
            processEvent { it.keyTyped(character) }

    override fun scrolled(amount: Int) =
            processEvent { it.scrolled(amount) }

    override fun keyUp(keycode: Int) =
            processEvent { it.keyUp(keycode) }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) =
            processEvent { it.touchDragged(screenX, screenY, pointer) }

    override fun keyDown(keycode: Int) =
            processEvent { it.keyDown(keycode) }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) =
            processEvent { it.touchDown(screenX, screenY, pointer, button) }

    private fun processEvent(func: (InputHandler) -> Boolean): Boolean {
        val handled = processors.firstOrNull { func(it) }
        return handled != null
    }

}
