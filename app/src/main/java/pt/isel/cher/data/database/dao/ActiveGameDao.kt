package pt.isel.cher.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.isel.cher.data.database.entities.ActiveGame
import pt.isel.cher.util.Constants.ACTIVE_GAME_ID

@Dao
interface ActiveGameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActiveGame(activeGame: ActiveGame)

    @Query("SELECT * FROM active_game WHERE id = :activeGameId")
    suspend fun getActiveGame(activeGameId: Int = ACTIVE_GAME_ID): ActiveGame?

    @Delete
    suspend fun deleteActiveGame(activeGame: ActiveGame)
}
