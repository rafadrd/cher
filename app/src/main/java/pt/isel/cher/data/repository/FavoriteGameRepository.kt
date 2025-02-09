package pt.isel.cher.data.repository

import kotlinx.coroutines.flow.Flow
import pt.isel.cher.data.database.dao.FavoriteGameDao
import pt.isel.cher.data.database.entities.FavoriteGame
import pt.isel.cher.data.database.entities.MoveEntity
import pt.isel.cher.data.database.relations.FavoriteGameWithMoves
import pt.isel.cher.domain.Game

class FavoriteGameRepository(
    private val favoriteGameDao: FavoriteGameDao,
) {
    fun getAllFavoriteGames(): Flow<List<FavoriteGameWithMoves>> =
        favoriteGameDao.getAllFavoriteGamesWithMoves()

    fun getFavoriteGameById(gameId: String): Flow<FavoriteGameWithMoves> =
        favoriteGameDao.getFavoriteGameWithMovesById(gameId)

    suspend fun addFavoriteGame(
        game: Game,
        title: String,
        opponentName: String,
    ) {
        val favoriteGame =
            FavoriteGame(
                id = game.id,
                title = title,
                opponentName = opponentName,
                dateTime = System.currentTimeMillis(),
            )

        val moveEntities =
            game.board.moves.mapIndexed { index, move ->
                MoveEntity(
                    favoriteGameId = game.id,
                    moveNumber = index + 1,
                    row = move.position.row,
                    col = move.position.col,
                    player = move.player.name,
                )
            }

        favoriteGameDao.insertFavoriteGameWithMoves(favoriteGame, moveEntities)
    }

    suspend fun deleteFavoriteGame(favoriteGame: FavoriteGame) {
        favoriteGameDao.deleteFavoriteGame(favoriteGame)
    }
}
