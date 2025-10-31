package pt.isel.cher.data.mapper

import pt.isel.cher.data.database.entities.ActiveGame
import pt.isel.cher.data.database.entities.FavoriteMove
import pt.isel.cher.data.database.relations.FavoriteGameWithMoves
import pt.isel.cher.domain.Board
import pt.isel.cher.domain.FavoriteGame
import pt.isel.cher.domain.FavoriteInfo
import pt.isel.cher.domain.Game
import pt.isel.cher.domain.Move
import pt.isel.cher.domain.Position

fun FavoriteMove.toDomain() =
    Move(position = Position(row = this.row, col = this.col), player = this.player)

fun FavoriteGameWithMoves.toFavoriteInfo() =
    FavoriteInfo(
        id = this.favoriteGame.id,
        title = this.favoriteGame.title,
        opponentName = this.favoriteGame.opponentName,
        dateTime = this.favoriteGame.dateTime,
    )

fun FavoriteGameWithMoves.toFavoriteGame() =
    FavoriteGame(
        info = this.toFavoriteInfo(),
        moves = this.moves.sortedBy { it.moveNumber }.map { it.toDomain() },
    )

fun Game.toFavoriteGameEntity(title: String, opponentName: String) =
    pt.isel.cher.data.database.entities.FavoriteGame(
        id = this.id,
        title = title,
        opponentName = opponentName,
        dateTime = System.currentTimeMillis(),
    )

fun Game.toFavoriteMoveEntities() =
    this.board.moves.mapIndexed { index, move ->
        FavoriteMove(
            favoriteGameId = this.id,
            moveNumber = index + 1,
            row = move.position.row,
            col = move.position.col,
            player = move.player,
        )
    }

fun Game.toActiveGameEntity() =
    ActiveGame(
        moves = this.board.moves,
        currentPlayer = this.currentPlayer,
        isOver = this.isOver,
        winner = this.winner,
    )

fun ActiveGame.toDomain(): Game {
    val reconstructedBoard =
        this.moves.fold(Board()) { board, move -> board.forcePlayMove(move.position, move.player) }
    return Game(
        board = reconstructedBoard,
        currentPlayer = this.currentPlayer,
        isOver = this.isOver,
        winner = this.winner,
    )
}
