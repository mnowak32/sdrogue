package pl.cdbr.sdrogue.desktop

import com.badlogic.gdx.Input.Keys
import pl.cdbr.sdrogue.game.InputEvent
import pl.cdbr.sdrogue.game.InputFlag
import pl.cdbr.sdrogue.game.InputHandler

class KbdHandler : InputHandler() {

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

            Keys.COMMA -> if (stateFlags.contains(InputFlag.SHIFT)) InputEvent.CLIMB_DOWN else null
            Keys.PERIOD -> if (stateFlags.contains(InputFlag.SHIFT))
                InputEvent.CLIMB_UP
            else
                InputEvent.MOVE_NONE

            Keys.ESCAPE -> InputEvent.CANCEL

            //special (mod) keys
            Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT -> { stateFlags += InputFlag.SHIFT; null }
            Keys.ALT_LEFT, Keys.ALT_RIGHT -> { stateFlags += InputFlag.ALT; null }
            Keys.CONTROL_LEFT, Keys.CONTROL_RIGHT -> { stateFlags += InputFlag.CTRL; null }

            else -> null
        }

        return if (evt != null) {
            feed(evt)
            true
        } else {
            false
        }
    }
    override fun keyTyped(character: Char) = false
    override fun keyUp(keycode: Int) = when (keycode) {
        Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT -> { stateFlags -= InputFlag.SHIFT; true }
        Keys.ALT_LEFT, Keys.ALT_RIGHT -> { stateFlags -= InputFlag.ALT; true }
        Keys.CONTROL_LEFT, Keys.CONTROL_RIGHT -> { stateFlags -= InputFlag.CTRL; true }
        else -> false
    }

    // Non-keyboard events, let it go...
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
    override fun mouseMoved(screenX: Int, screenY: Int) = false
    override fun scrolled(amount: Int) = false
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = false
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = false
}