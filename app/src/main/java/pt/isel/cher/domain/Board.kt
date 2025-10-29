package pt.isel.cher.domain

import pt.isel.cher.util.Constants

data class Board(
    val moves: List<Move> = emptyList(),
    val grid: List<List<Player?>> = initializeGrid(),
) {
    companion object {
        fun initializeGrid(): List<List<Player?>> {
            val grid =
                List(Constants.BOARD_SIZE) { MutableList<Player?>(Constants.BOARD_SIZE) { null } }
            grid[3][3] = Player.WHITE
            grid[4][4] = Player.WHITE
            grid[3][4] = Player.BLACK
            grid[4][3] = Player.BLACK
            return grid
        }
    }

    fun playMove(position: Position, player: Player): Board? {
        if (!isValidMove(position, player)) return null

        val newGrid = grid.map { it.toMutableList() }
        newGrid[position.row][position.col] = player

        Constants.DIRECTIONS.forEach { direction ->
            collectPiecesToFlip(position, direction, player)?.forEach { pos ->
                newGrid[pos.row][pos.col] = player
            }
        }

        return copy(moves = moves + Move(position, player), grid = newGrid.map { it.toList() })
    }

    private fun collectPiecesToFlip(
        start: Position,
        direction: Direction,
        player: Player,
    ): List<Position>? {
        val pieces = mutableListOf<Position>()
        var currentRow = start.row + direction.deltaRow
        var currentCol = start.col + direction.deltaCol

        while (isWithinBounds(currentRow, currentCol)) {
            when (grid[currentRow][currentCol]) {
                player.opponent -> pieces.add(Position(currentRow, currentCol))
                player -> return pieces.ifEmpty { null }
                else -> break
            }
            currentRow += direction.deltaRow
            currentCol += direction.deltaCol
        }
        return null
    }

    private fun isValidMove(position: Position, player: Player): Boolean {
        if (
            !isWithinBounds(position.row, position.col) || grid[position.row][position.col] != null
        ) {
            return false
        }

        return Constants.DIRECTIONS.any { direction ->
            var currentRow = position.row + direction.deltaRow
            var currentCol = position.col + direction.deltaCol
            var hasOpponentBetween = false

            while (isWithinBounds(currentRow, currentCol)) {
                when (grid[currentRow][currentCol]) {
                    player.opponent -> hasOpponentBetween = true
                    player -> return@any hasOpponentBetween
                    else -> break
                }
                currentRow += direction.deltaRow
                currentCol += direction.deltaCol
            }
            false
        }
    }

    fun score(player: Player): Int {
        val score = grid.sumOf { row -> row.count { it == player } }
        return score
    }

    fun hasValidMoves(player: Player): Boolean =
        grid.indices.any { row ->
            grid[row].indices.any { col -> isValidMove(Position(row, col), player) }
        }

    fun isGameOver(): Boolean =
        (!hasValidMoves(Player.BLACK) && !hasValidMoves(Player.WHITE)) ||
            moves.size == Constants.BOARD_SIZE * Constants.BOARD_SIZE

    private fun isWithinBounds(row: Int, col: Int): Boolean =
        row in 0 until Constants.BOARD_SIZE && col in 0 until Constants.BOARD_SIZE
}
