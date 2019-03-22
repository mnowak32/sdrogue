package pl.cdbr.sdrogue.game

import com.badlogic.gdx.Gdx
import pl.cdbr.sdrogue.GameConfig
import pl.cdbr.sdrogue.game.input.InputEvent
import pl.cdbr.sdrogue.game.input.InputFlag

class Engine(private val st: State) {
    fun tick() {
        if (st.eventWaiting()) {
            val ev = st.event()
            dispatchEvent(ev)
            if (ev.doTurn) {
                st.currentTick++
                //TODO
            }
        } else {
            // whadda do?
        }
    }

    private fun dispatchEvent(ev: InputEvent) {
        when (ev) {
            InputEvent.MOVE_N,
            InputEvent.MOVE_NE,
            InputEvent.MOVE_E,
            InputEvent.MOVE_SE,
            InputEvent.MOVE_S,
            InputEvent.MOVE_SW,
            InputEvent.MOVE_W,
            InputEvent.MOVE_NW
                    -> doMove(ev)

            InputEvent.CLIMB_DOWN,
            InputEvent.CLIMB_UP
                    -> doClimb(ev)

            InputEvent.CANCEL
                    -> doCancel()

            InputEvent.K_SHIFT_D,
            InputEvent.K_SHIFT_U,
            InputEvent.K_CTRL_D,
            InputEvent.K_CTRL_U,
            InputEvent.K_ALT_D,
            InputEvent.K_ALT_U
                -> doInternal(ev)

            else -> {}
        }
    }

    private fun doInternal(ev: InputEvent) {
        when (ev) {
            InputEvent.K_SHIFT_D -> st.inputFlags += InputFlag.SHIFT
            InputEvent.K_SHIFT_U -> st.inputFlags -= InputFlag.SHIFT
            InputEvent.K_CTRL_D -> st.inputFlags += InputFlag.CTRL
            InputEvent.K_CTRL_U -> st.inputFlags -= InputFlag.CTRL
            InputEvent.K_ALT_D -> st.inputFlags += InputFlag.ALT
            InputEvent.K_ALT_U -> st.inputFlags -= InputFlag.ALT

            else -> {}
        }
    }

    private fun doClimb(ev: InputEvent) {
        st.changeLevel(ev == InputEvent.CLIMB_DOWN)
    }

    private fun doCancel() {
        Gdx.app.exit()
    }

    private fun doMove(ev: InputEvent) {
        val dY = when(ev) {
            InputEvent.MOVE_N, InputEvent.MOVE_NE, InputEvent.MOVE_NW -> 1
            InputEvent.MOVE_S, InputEvent.MOVE_SE, InputEvent.MOVE_SW -> -1
            else -> 0
        }
        val dX = when(ev) {
            InputEvent.MOVE_E, InputEvent.MOVE_NE, InputEvent.MOVE_SE -> 1
            InputEvent.MOVE_W, InputEvent.MOVE_SW, InputEvent.MOVE_NW -> -1
            else -> 0
        }

        st.move(dX, dY)
        st.dirty()
    }

}