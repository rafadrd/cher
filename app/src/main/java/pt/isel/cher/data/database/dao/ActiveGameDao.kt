package pt.isel.cher.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.isel.cher.data.database.entities.ActiveGame
import pt.isel.cher.util.Constants

@Dao
interface ActiveGameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveActiveGame(activeGame: ActiveGame)

    @Query("SELECT * FROM active_game WHERE id = :activeGameId")
    suspend fun getActiveGame(activeGameId: Int = Constants.ACTIVE_GAME_ID): ActiveGame?

    @Query("DELETE FROM active_game WHERE id = :activeGameId")
    suspend fun deleteActiveGame(activeGameId: Int = Constants.ACTIVE_GAME_ID)
}
