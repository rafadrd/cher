package pt.isel.cher.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.isel.cher.domain.Player
import pt.isel.cher.util.Constants

@Entity(tableName = "active_game")
data class ActiveGame(
    @PrimaryKey val id: Int = Constants.ACTIVE_GAME_ID,
    val grid: List<List<Player?>>,
    val currentPlayer: Player,
    val isOver: Boolean,
    val winner: Player?,
)
