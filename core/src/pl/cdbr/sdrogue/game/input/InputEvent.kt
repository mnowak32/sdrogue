package pl.cdbr.sdrogue.game.input

/**
 * Various user-triggered events.
 * @param doTurn specifies whether the input causes game time to progress by a turn.
 */
enum class InputEvent(val doTurn: Boolean = true) {
    //basic movement inputs
    MOVE_N, MOVE_E, MOVE_S, MOVE_W,
    //diagonal movement inputs
    MOVE_NE, MOVE_SE, MOVE_SW, MOVE_NW,
    //no movement (pass a turn)
    MOVE_NONE,

    //moving between levels
    CLIMB_UP, CLIMB_DOWN,

    //use items, weapon, magic
    USE1, USE2, FIRE, CAST, RELOAD,

    //switch weapons, spells, etc. (no time progression)
    SW_WP_1(false), SW_WP_2(false), SW_WP_3(false), SW_WP_4(false), SW_WP_5(false),
    SW_SP_1(false), SW_SP_2(false), SW_SP_3(false), SW_SP_4(false), SW_SP_5(false),

    //open in-game screens (no time progress)
    OPEN_INV(false), OPEN_EQUIP(false), OPEN_MENU(false),

    //
    CANCEL(false)
}