package pt.isel.cher.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.isel.cher.util.Constants.ACTIVE_GAME_ID

@Entity(tableName = "active_game")
data class ActiveGame(
    @PrimaryKey val id: Int = ACTIVE_GAME_ID,
    val currentPlayer: String, // "BLACK" or "WHITE"
    val isOver: Boolean,
    val winner: String?, // "BLACK", "WHITE" or null for a tie
)
