package pl.cdbr.sdrogue.game

import com.badlogic.gdx.InputProcessor

abstract class InputHandler: InputProcessor {
    val stateFlags = mutableSetOf<InputFlag>()
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

    protected fun feed(ev: InputEvent) {
        consumers.forEach { it.consume(ev) }
    }
}

interface InputEventConsumer {
    fun consume(evt: InputEvent)
}

enum class InputFlag { SHIFT, CTRL, ALT, TWO_FINGER }
