package pt.isel.cher.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import pt.isel.cher.data.database.entities.FavoriteGame
import pt.isel.cher.data.database.entities.FavoriteMove
import pt.isel.cher.data.database.relations.FavoriteGameWithMoves

@Dao
interface FavoriteGameDao {
    @Transaction
    @Query("SELECT * FROM favorite_games ORDER BY dateTime DESC")
    fun getAllFavoriteGamesWithMoves(): Flow<List<FavoriteGameWithMoves>>

    @Transaction
    @Query("SELECT * FROM favorite_games WHERE id = :gameId")
    fun getFavoriteGameWithMovesById(gameId: String): Flow<FavoriteGameWithMoves>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteGame(favoriteGame: FavoriteGame)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoves(moves: List<FavoriteMove>)

    @Transaction
    suspend fun insertFavoriteGameWithMoves(favoriteGame: FavoriteGame, moves: List<FavoriteMove>) {
        insertFavoriteGame(favoriteGame)
        insertMoves(moves)
    }

    @Delete suspend fun deleteFavoriteGame(favoriteGame: FavoriteGame)

    @Query("SELECT COUNT(*) FROM favorite_games WHERE id = :gameId")
    suspend fun countById(gameId: String): Int

    @Query("DELETE FROM favorite_games WHERE id = :gameId")
    suspend fun deleteFavoriteGameById(gameId: String)
}
