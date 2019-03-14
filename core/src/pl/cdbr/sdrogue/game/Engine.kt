package pl.cdbr.sdrogue.game

import com.badlogic.gdx.Gdx
import pl.cdbr.sdrogue.GameConfig
import pl.cdbr.sdrogue.game.input.InputEvent

class Engine(private val st: State, private val gc: GameConfig) {
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