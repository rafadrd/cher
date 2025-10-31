package pt.isel.cher.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.isel.cher.domain.Move
import pt.isel.cher.domain.Player

@Entity(tableName = "active_game")
data class ActiveGame(
    @PrimaryKey val id: Int = ACTIVE_GAME_ID,
    val moves: List<Move>,
    val currentPlayer: Player,
    val isOver: Boolean,
    val winner: Player?,
) {
    companion object {
        const val ACTIVE_GAME_ID = 1
    }
}
