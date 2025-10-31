package pt.isel.cher.domain

data class Board(
    val moves: List<Move> = emptyList(),
    val grid: List<List<Player?>> = initializeGrid(),
) {
    companion object {
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

        fun initializeGrid(): List<List<Player?>> {
            val grid = List(BOARD_SIZE) { MutableList<Player?>(BOARD_SIZE) { null } }
            grid[3][3] = Player.WHITE
            grid[4][4] = Player.WHITE
            grid[3][4] = Player.BLACK
            grid[4][3] = Player.BLACK
            return grid
        }

        fun getValidMoves(board: Board, player: Player): Set<Position> {
            return board.grid.indices
                .flatMap { row ->
                    board.grid[row].indices.mapNotNull { col ->
                        val pos = Position(row, col)
                        if (board.isValidMove(pos, player)) pos else null
                    }
                }
                .toSet()
        }
    }

    fun playMove(position: Position, player: Player): Board? {
        if (!isValidMove(position, player)) return null
        return applyMove(position, player)
    }

    fun forcePlayMove(position: Position, player: Player): Board {
        return applyMove(position, player)
    }

    private fun applyMove(position: Position, player: Player): Board {
        val newGrid = grid.map { it.toMutableList() }
        newGrid[position.row][position.col] = player

        DIRECTIONS.forEach { direction ->
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

        return DIRECTIONS.any { direction ->
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
            moves.size == BOARD_SIZE * BOARD_SIZE

    private fun isWithinBounds(row: Int, col: Int): Boolean =
        row in 0 until BOARD_SIZE && col in 0 until BOARD_SIZE
}
