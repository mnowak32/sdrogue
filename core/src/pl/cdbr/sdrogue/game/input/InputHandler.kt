package pl.cdbr.sdrogue.game.input

import com.badlogic.gdx.InputProcessor

abstract class InputHandler: InputProcessor {
    val consumers = mutableListOf<InputEventConsumer>()

    //keyboard
    override fun keyDown(keycode: Int) = false
    override fun keyTyped(character: Char) = false
    override fun keyUp(keycode: Int) = false

    //touch/mouse
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = false
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = false

    //mouse
    override fun mouseMoved(screenX: Int, screenY: Int) = false
    override fun scrolled(amount: Int) = false

    protected fun feed(ev: InputEvent, flags: Set<InputFlag> = emptySet()) {
        consumers.forEach { it.consume(ev, flags) }
    }
}

interface InputEventConsumer {
    fun consume(evt: InputEvent, flags: Set<InputFlag>)
}

enum class InputFlag { SHIFT, CTRL, ALT, TWO_FINGER }
