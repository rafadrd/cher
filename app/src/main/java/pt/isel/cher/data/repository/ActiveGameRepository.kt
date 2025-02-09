package pt.isel.cher.data.repository

import pt.isel.cher.data.database.dao.ActiveGameDao
import pt.isel.cher.data.database.dao.ActiveMoveDao
import pt.isel.cher.data.database.dao.BoardCellDao
import pt.isel.cher.data.database.entities.ActiveGame
import pt.isel.cher.data.database.entities.ActiveMoveEntity
import pt.isel.cher.data.database.entities.BoardCell
import pt.isel.cher.domain.Board
import pt.isel.cher.domain.Game
import pt.isel.cher.domain.Move
import pt.isel.cher.domain.Player
import pt.isel.cher.domain.Position
import pt.isel.cher.util.Constants.ACTIVE_GAME_ID
import pt.isel.cher.util.Constants.BOARD_SIZE

class ActiveGameRepository(
    private val activeGameDao: ActiveGameDao,
    private val activeMoveDao: ActiveMoveDao,
    private val boardCellDao: BoardCellDao,
) {
    suspend fun getActiveGame(): Game? {
        val activeGameEntity = activeGameDao.getActiveGame(ACTIVE_GAME_ID) ?: return null
        val activeMoves = activeMoveDao.getActiveMoves(ACTIVE_GAME_ID)
        val boardCells = boardCellDao.getBoardCells(ACTIVE_GAME_ID)

        val grid =
            List(BOARD_SIZE) { row ->
                List(BOARD_SIZE) { col ->
                    boardCells.find { it.row == row && it.col == col }?.player?.let {
                        Player.valueOf(it)
                    }
                }
            }

        val board =
            Board(
                moves =
                    activeMoves.map { moveEntity ->
                        Move(
                            position = Position(row = moveEntity.row, col = moveEntity.col),
                            player = Player.valueOf(moveEntity.player),
                        )
                    },
                grid = grid,
            )

        return Game(
            id = "ACTIVE_GAME",
            board = board,
            currentPlayer = Player.valueOf(activeGameEntity.currentPlayer),
            isOver = activeGameEntity.isOver,
            winner = activeGameEntity.winner?.let { Player.valueOf(it) },
        )
    }

    suspend fun saveActiveGame(game: Game) {
        activeGameDao.insertActiveGame(
            ActiveGame(
                id = ACTIVE_GAME_ID,
                currentPlayer = game.currentPlayer.name,
                isOver = game.isOver,
                winner = game.winner?.name,
            ),
        )

        val activeMoveEntities =
            game.board.moves.mapIndexed { index, move ->
                ActiveMoveEntity(
                    activeGameId = 1,
                    moveNumber = index + 1,
                    row = move.position.row,
                    col = move.position.col,
                    player = move.player.name,
                )
            }
        activeMoveDao.insertActiveMoves(activeMoveEntities)

        val boardCells = mutableListOf<BoardCell>()
        for (row in 0 until BOARD_SIZE) {
            for (col in 0 until BOARD_SIZE) {
                boardCells.add(
                    BoardCell(
                        activeGameId = ACTIVE_GAME_ID,
                        row = row,
                        col = col,
                        player = game.board.grid[row][col]?.name,
                    ),
                )
            }
        }
        boardCellDao.insertBoardCells(boardCells)
    }

    suspend fun clearActiveGame() {
        val activeGame = activeGameDao.getActiveGame(ACTIVE_GAME_ID) ?: return
        activeGameDao.deleteActiveGame(activeGame)
    }
}
