package pt.isel.cher.domain

import kotlinx.serialization.Serializable

@Serializable
data class Position(val row: Int, val col: Int) {
    init {
        require(row in 0 until Board.BOARD_SIZE && col in 0 until Board.BOARD_SIZE) {
            "Position ($row, $col) must be within 0 to ${Board.BOARD_SIZE - 1} for both row and column."
        }
    }
}
