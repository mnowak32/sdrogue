package pl.cdbr.sdrogue.desktop

import com.badlogic.gdx.Input
import pl.cdbr.sdrogue.game.input.InputEvent
import pl.cdbr.sdrogue.game.input.InputHandler

class MouseHandler : InputHandler() {
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val evt = when (button) {
            Input.Buttons.LEFT -> InputEvent.CLIMB_DOWN
            Input.Buttons.RIGHT -> InputEvent.CLIMB_UP
            else -> null
        }

        return if (evt != null) {
            feed(evt)
            true
        } else {
            false
        }
    }
}