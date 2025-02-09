package pt.isel.cher.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.isel.cher.data.database.entities.ActiveMoveEntity
import pt.isel.cher.util.Constants.ACTIVE_GAME_ID

@Dao
interface ActiveMoveDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActiveMoves(moves: List<ActiveMoveEntity>)

    @Query("SELECT * FROM active_moves WHERE activeGameId = :activeGameId ORDER BY moveNumber ASC")
    fun getActiveMoves(activeGameId: Int = ACTIVE_GAME_ID): List<ActiveMoveEntity>
}
