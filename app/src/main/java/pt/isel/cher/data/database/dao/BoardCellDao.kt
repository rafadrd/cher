package pt.isel.cher.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pt.isel.cher.data.database.entities.BoardCell
import pt.isel.cher.util.Constants.ACTIVE_GAME_ID

@Dao
interface BoardCellDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoardCells(cells: List<BoardCell>)

    @Query("SELECT * FROM active_board_cells WHERE activeGameId = :activeGameId")
    fun getBoardCells(activeGameId: Int = ACTIVE_GAME_ID): List<BoardCell>
}
