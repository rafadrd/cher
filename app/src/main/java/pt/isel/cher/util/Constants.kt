package pt.isel.cher.util

import pt.isel.cher.domain.Direction

object Constants {
    const val TAG = "CHER"
    const val BOARD_SIZE = 8
    val DIRECTIONS =
        listOf(
            Direction(-1, 0),
            Direction(-1, 1),
            Direction(0, 1),
            Direction(1, 1),
            Direction(1, 0),
            Direction(1, -1),
            Direction(0, -1),
            Direction(-1, -1),
        )
    const val ACTIVE_GAME_ID = 1
}
