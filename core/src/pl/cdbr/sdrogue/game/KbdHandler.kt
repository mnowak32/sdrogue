package pl.cdbr.sdrogue.game

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputProcessor

class KbdHandler(private val state: State) : InputProcessor {
    var shiftPressed = false
    var ctrlPressed = false
    var altPressed = false

    // Don't know which one to use for now...
    override fun keyDown(keycode: Int): Boolean {
        val evt = when (keycode) {
            Keys.LEFT, Keys.H, Keys.NUMPAD_4 -> InputEvent.MOVE_W
            Keys.UP, Keys.J, Keys.NUMPAD_8 -> InputEvent.MOVE_N
            Keys.RIGHT, Keys.L, Keys.NUMPAD_6 -> InputEvent.MOVE_E
            Keys.DOWN, Keys.K, Keys.NUMPAD_2 -> InputEvent.MOVE_S
            Keys.NUMPAD_7 -> InputEvent.MOVE_NW
            Keys.NUMPAD_9 -> InputEvent.MOVE_NE
            Keys.NUMPAD_3 -> InputEvent.MOVE_SE
            Keys.NUMPAD_1 -> InputEvent.MOVE_SW

            Keys.COMMA -> if (shiftPressed) InputEvent.CLIMB_DOWN else null
            Keys.PERIOD -> if (shiftPressed)
                InputEvent.CLIMB_UP
            else
                InputEvent.MOVE_NONE

            Keys.ESCAPE -> InputEvent.CANCEL

            //special (mod) keys
            Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT -> { shiftPressed = true; null }
            Keys.ALT_LEFT, Keys.ALT_RIGHT -> { altPressed = true; null }
            Keys.CONTROL_LEFT, Keys.CONTROL_RIGHT -> { ctrlPressed = true; null }

            else -> null
        }

        return if (evt != null) {
            state.queueEvent(evt)
            true
        } else {
            false
        }
    }
    override fun keyTyped(character: Char) = false
    override fun keyUp(keycode: Int) = when (keycode) {
        Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT -> { shiftPressed = false; true }
        Keys.ALT_LEFT, Keys.ALT_RIGHT -> { altPressed = false; true }
        Keys.CONTROL_LEFT, Keys.CONTROL_RIGHT -> { ctrlPressed = false; true }
        else -> false
    }

    // Non-keyboard events, let it go...
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
    override fun mouseMoved(screenX: Int, screenY: Int) = false
    override fun scrolled(amount: Int) = false
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = false
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
}