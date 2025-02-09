package pt.isel.cher.domain

import java.util.UUID

data class Game(
    val id: String = UUID.randomUUID().toString(),
    val board: Board = Board(),
    val currentPlayer: Player = Player.BLACK,
    val isOver: Boolean = false,
    val winner: Player? = null,
) {
    fun playMove(position: Position): Game {
        if (isOver) return this

        val newBoard = board.playMove(position, currentPlayer) ?: return this
        val nextPlayer = determineNextPlayer(newBoard, currentPlayer)
        val gameOver = newBoard.isGameOver()
        val newWinner = if (gameOver) determineWinner(newBoard) else null

        return copy(
            board = newBoard,
            currentPlayer = nextPlayer ?: currentPlayer,
            isOver = gameOver,
            winner = newWinner,
        )
    }

    private fun determineWinner(board: Board): Player? {
        val blackScore = board.score(Player.BLACK)
        val whiteScore = board.score(Player.WHITE)
        println("Final Black Score: $blackScore, Final White Score: $whiteScore")
        return when {
            blackScore > whiteScore -> Player.BLACK
            whiteScore > blackScore -> Player.WHITE
            else -> null
        }
    }

    private fun determineNextPlayer(
        board: Board,
        currentPlayer: Player,
    ): Player? =
        when {
            board.hasValidMoves(currentPlayer.opponent) -> currentPlayer.opponent
            board.hasValidMoves(currentPlayer) -> currentPlayer
            else -> null
        }
}
