package pl.cdbr.sdrogue.game

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputProcessor

class KbdHandler(val state: State) : InputProcessor {
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
//            Keys.NUMPAD_5, Keys.PERIOD -> InputEvent.MOVE_NONE

            Keys.COMMA -> InputEvent.CLIMB_DOWN
            Keys.PERIOD -> InputEvent.CLIMB_UP

            Keys.ESCAPE -> InputEvent.CANCEL
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
    override fun keyUp(keycode: Int) = false

    // Non-keyboard events, let it go...
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
    override fun mouseMoved(screenX: Int, screenY: Int) = false
    override fun scrolled(amount: Int) = false
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = false
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
}