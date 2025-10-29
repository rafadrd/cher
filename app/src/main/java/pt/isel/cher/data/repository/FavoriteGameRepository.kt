package pt.isel.cher.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pt.isel.cher.data.database.dao.FavoriteGameDao
import pt.isel.cher.data.mapper.toFavoriteGame
import pt.isel.cher.data.mapper.toFavoriteGameEntity
import pt.isel.cher.data.mapper.toFavoriteInfo
import pt.isel.cher.data.mapper.toFavoriteMoveEntities
import pt.isel.cher.domain.FavoriteGame
import pt.isel.cher.domain.FavoriteInfo
import pt.isel.cher.domain.Game
import javax.inject.Inject

class FavoriteGameRepository @Inject constructor(private val favoriteGameDao: FavoriteGameDao) {
    fun getAllFavoriteGames(): Flow<List<FavoriteInfo>> =
        favoriteGameDao.getAllFavoriteGamesWithMoves().map { list ->
            list.map { it.toFavoriteInfo() }
        }

    fun getFavoriteGameById(gameId: String): Flow<FavoriteGame> =
        favoriteGameDao.getFavoriteGameWithMovesById(gameId).map { it.toFavoriteGame() }

    suspend fun addFavoriteGame(game: Game, title: String, opponentName: String) {
        val favoriteGame = game.toFavoriteGameEntity(title, opponentName)
        val moveEntities = game.toFavoriteMoveEntities()

        favoriteGameDao.insertFavoriteGameWithMoves(favoriteGame, moveEntities)
    }

    suspend fun isGameFavorited(gameId: String): Boolean = favoriteGameDao.countById(gameId) > 0

    suspend fun deleteFavoriteGame(gameInfo: FavoriteInfo) {
        favoriteGameDao.deleteFavoriteGameById(gameInfo.id)
    }
}
