package pt.isel.cher.domain

import pt.isel.cher.util.Constants

data class Position(val row: Int, val col: Int) {
    init {
        require(row in 0 until Constants.BOARD_SIZE && col in 0 until Constants.BOARD_SIZE) {
            "Position ($row, $col) must be within 0 to ${Constants.BOARD_SIZE - 1} for both row and column."
        }
    }
}
